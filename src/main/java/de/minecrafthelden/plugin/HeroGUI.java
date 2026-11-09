package de.minecrafthelden.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class HeroGUI implements InventoryHolder {
    private final Inventory inventory;

    public HeroGUI(org.bukkit.entity.Player player, HeroManager manager) {
        inventory = Bukkit.createInventory(this, 27, "§6§lMinecraft Helden");
        int slot = 10;
        for (Hero hero : manager.getHeroes()) {
            ItemStack item = new ItemStack(Material.DIAMOND);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(hero.getDisplayName());
            List<String> lore = new ArrayList<>();
            lore.add("§7Gesundheit: §e" + hero.getBaseHealth());
            lore.add("§7Schaden: §e" + hero.getBaseDamage());
            lore.add("§7Geschwindigkeit: §e" + hero.getBaseSpeed());
            lore.add("§aKlicke, um diesen Helden zu wählen.");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(slot++, item);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
