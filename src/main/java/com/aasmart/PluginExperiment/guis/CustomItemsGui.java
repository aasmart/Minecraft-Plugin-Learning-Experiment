package com.aasmart.PluginExperiment.guis;

import com.aasmart.PluginExperiment.InteractableItems.InteractableItem;
import com.aasmart.PluginExperiment.InteractableItems.Items.FlameStick;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class CustomItemsGui extends GUI implements Listener {
    private final Inventory gui;

    public CustomItemsGui() {
        gui = Bukkit.createInventory(null, 27, Component.text("Custom Items"));

        initializeItems();
    }

    public void initializeItems() {
        InteractableItem.getItems().values().forEach(item -> gui.addItem(item.getCustomItem()));
    }

    public void open(HumanEntity player) {
        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if(!event.getClickedInventory().equals(gui))
            return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        if(event.getCurrentItem() != null)
            player.getInventory().addItem(event.getCurrentItem());
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent event) {
        if (event.getInventory().equals(gui)) {
            event.setCancelled(true);
        }
    }

    public static CustomItemsGui initializeGUI(PluginManager manager, Plugin plugin) {
        CustomItemsGui gui = new CustomItemsGui();
        manager.registerEvents(gui, plugin);
        return gui;
    }
}
