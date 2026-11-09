package de.minecrafthelden.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class MinecraftHeldenPlugin extends JavaPlugin {
    private HeroManager heroManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        heroManager = new HeroManager(this);
        getServer().getPluginManager().registerEvents(new HeroGUIListener(heroManager), this);
        getCommand("helden").setExecutor(this);
        getLogger().info("Minecraft Helden wurde aktiviert!");
    }

    @Override
    public void onDisable() {
        if (heroManager != null) heroManager.saveAll();
        getLogger().info("Minecraft Helden wurde deaktiviert.");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Dieser Befehl kann nur im Spiel verwendet werden.");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("liste")) {
            heroManager.openHeroGUI(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "waehlen", "wählen", "choose" -> {
                if (args.length < 2) {
                    player.sendMessage("§cVerwendung: /helden waehlen <held>");
                } else {
                    heroManager.chooseHero(player, args[1]);
                }
            }
            case "info" -> heroManager.showHeroInfo(player);
            default -> player.sendMessage("§cVerwendung: /helden <liste|waehlen <held>|info>");
        }
        return true;
    }

    public HeroManager getHeroManager() {
        return heroManager;
    }
}
