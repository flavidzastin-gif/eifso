package de.minecrafthelden.plugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class HeroManager {
    private final MinecraftHeldenPlugin plugin;
    private final Map<String, Hero> heroes = new LinkedHashMap<>();
    private final Map<UUID, PlayerHeroData> playerData = new HashMap<>();
    private final File dataFolder;

    public HeroManager(MinecraftHeldenPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "players");
        if (!dataFolder.exists()) dataFolder.mkdirs();
        loadDefaultHeroes();
        loadPlayerData();
    }

    private void loadDefaultHeroes() {
        Hero warrior = new Hero("krieger", "§cKrieger", 100, 8.0, 0.25);
        warrior.addSkill(new Skill("tausch", "§eTausch", "Tauscht Gegenstände (Funktion folgt).", 60));
        Hero magenta = new Hero("magenta", "§5Magenta", 80, 12.0, 0.28);
        magenta.addSkill(new Skill("blitz", "§dBlitz", "Blitz-Fähigkeit (Funktion folgt).", 40));
        Hero mage = new Hero("magier", "§9Magier", 60, 6.0, 0.22);
        mage.addSkill(new Skill("heilung", "§bHeilung", "Heilungs-Fähigkeit (Funktion folgt).", 90));
        heroes.put(warrior.getName(), warrior);
        heroes.put(magenta.getName(), magenta);
        heroes.put(mage.getName(), mage);
    }

    public void openHeroGUI(Player player) {
        HeroGUI gui = new HeroGUI(player, this);
        player.openInventory(gui.getInventory());
    }

    public void chooseHero(Player player, String heroName) {
        Hero hero = heroes.get(heroName.toLowerCase(Locale.ROOT));
        if (hero == null) {
            player.sendMessage("§cDieser Held existiert nicht. Nutze /helden liste.");
            return;
        }
        PlayerHeroData data = getPlayerData(player.getUniqueId());
        data.setHeroName(hero.getName());
        savePlayerData(data);
        player.sendMessage("§aDu hast " + hero.getDisplayName() + " §agewählt!");
    }

    public void showHeroInfo(Player player) {
        PlayerHeroData data = getPlayerData(player.getUniqueId());
        Hero hero = data.getHeroName() == null ? null : heroes.get(data.getHeroName());
        if (hero == null) {
            player.sendMessage("§cDu hast noch keinen Helden gewählt. Nutze /helden liste.");
            return;
        }
        player.sendMessage("§6§lMinecraft Helden §8» §eDeine Helden-Info");
        player.sendMessage("§7Held: " + hero.getDisplayName());
        player.sendMessage("§7Level: §e" + data.getLevel());
        player.sendMessage("§7Erfahrung: §e" + data.getExperience() + "§7/§e" + data.getRequiredExperience());
        player.sendMessage("§7Basis-Gesundheit: §e" + hero.getBaseHealth());
        player.sendMessage("§7Basis-Schaden: §e" + hero.getBaseDamage());
        player.sendMessage("§7Basis-Geschwindigkeit: §e" + hero.getBaseSpeed());
        player.sendMessage("§6Fähigkeiten:");
        for (Skill skill : hero.getSkills().values()) {
            player.sendMessage("§7- " + skill.getDisplayName() + " §8» §7" + skill.getDescription()
                    + " §8(Cooldown: " + skill.getCooldown() + "s)");
        }
    }

    public void handleGuiClick(Player player, int rawSlot) {
        int index = rawSlot - 10;
        if (index < 0 || index >= heroes.size()) return;
        Hero hero = new ArrayList<>(heroes.values()).get(index);
        chooseHero(player, hero.getName());
        player.closeInventory();
    }

    public Collection<Hero> getHeroes() {
        return Collections.unmodifiableCollection(heroes.values());
    }

    public PlayerHeroData getPlayerData(UUID uuid) {
        return playerData.computeIfAbsent(uuid, PlayerHeroData::new);
    }

    public void saveAll() {
        for (PlayerHeroData data : playerData.values()) savePlayerData(data);
    }

    private void loadPlayerData() {
        File[] files = dataFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;
        for (File file : files) {
            try {
                UUID uuid = UUID.fromString(file.getName().replace(".yml", ""));
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                PlayerHeroData data = new PlayerHeroData(uuid);
                data.setHeroName(yaml.getString("hero"));
                // Fortschritt wird separat geladen.
                for (int i = 1; i < yaml.getInt("level", 1); i++) {
                    data.addExperience(data.getRequiredExperience());
                }
                data.addExperience(yaml.getInt("experience", 0));
                playerData.put(uuid, data);
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().warning("Ungültige Spielerdaten-Datei: " + file.getName());
            }
        }
    }

    private void savePlayerData(PlayerHeroData data) {
        File file = new File(dataFolder, data.getUuid() + ".yml");
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("hero", data.getHeroName());
        yaml.set("level", data.getLevel());
        yaml.set("experience", data.getExperience());
        try {
            yaml.save(file);
        } catch (IOException ex) {
            plugin.getLogger().severe("Spielerdaten konnten nicht gespeichert werden: " + file.getName());
            ex.printStackTrace();
        }
    }
}
