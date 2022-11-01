package com.aasmart.PluginExperiment.InteractableItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class InteractableItem {
    private boolean hasLeftFunction;
    private boolean hasRightFunction;
    private ItemStack customItem;

    public InteractableItem(boolean hasLeftFunction, boolean hasRightFunction) {
        this.hasLeftFunction = hasLeftFunction;
        this.hasRightFunction = hasRightFunction;
        this.customItem = createCustomItem();
    }

    private static final LinkedHashMap<Class<? extends InteractableItem>, InteractableItem> items = new LinkedHashMap<>();

    public abstract boolean canUseItem(PlayerInteractEvent event);

    public void useItem(PlayerInteractEvent event) {
        if(hasLeftFunction && event.getAction().isLeftClick())
            leftUse(event);
        else if(hasRightFunction && event.getAction().isRightClick())
            rightUse(event);
    }

    public abstract void leftUse(PlayerInteractEvent event);

    public abstract void rightUse(PlayerInteractEvent event);

    public static void addItem(InteractableItem item) {
        items.put(item.getClass(), item);
    }

    public static LinkedHashMap<Class<? extends InteractableItem>, InteractableItem> getItems() {
        return items;
    }

    public static InteractableItem getItem(Class<? extends InteractableItem> clazz) {
        return items.get(clazz);
    }

    public abstract ItemStack createCustomItem();

    public ItemStack getCustomItem() {
        return customItem;
    }

    public static class ItemUtils {
        public static boolean hasTag(ItemStack item, String key, String tag) {
            var itemNBT = CraftItemStack.asNMSCopy(item);
            CompoundTag itemTag = (itemNBT.hasTag()) ? itemNBT.getTag() : null;
            if(itemTag != null)
                return itemTag.getString(key).equals(tag);
            return false;
        }
    }
}
