package com.aasmart.PluginExperiment.handlers;

import com.aasmart.PluginExperiment.InteractableItems.InteractableItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemInteractHandler implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        determineUsableItem(event);
    }

    private static void determineUsableItem(PlayerInteractEvent event) {
        var items = InteractableItem.getItems();
        for(InteractableItem i : items.values()) {
            if(i.canUseItem(event)) {
                i.useItem(event);
                break;
            }
        }
    }
}
