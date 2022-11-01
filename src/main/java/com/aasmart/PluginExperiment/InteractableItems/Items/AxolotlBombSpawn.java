package com.aasmart.PluginExperiment.InteractableItems.Items;

import com.aasmart.PluginExperiment.InteractableItems.InteractableItem;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AxolotlBombSpawn extends InteractableItem {
    public AxolotlBombSpawn(boolean hasLeftFunction, boolean hasRightFunction) {
        super(hasLeftFunction, hasRightFunction);
    }

    @Override
    public boolean canUseItem(PlayerInteractEvent event) {
        if(event.getItem() == null)
            return false;

        ItemStack eventItem = event.getItem();
        return eventItem.getType() == getCustomItem().getType()
                && ItemUtils.hasTag(eventItem, "itemtype", "axolotlbombspawn");
    }

    @Override
    public void leftUse(PlayerInteractEvent event) {

    }

    @Override
    public void rightUse(PlayerInteractEvent event) {
        //event.setCancelled(true);

        /*if(event.getInteractionPoint() == null)
            return;
        Location placeLocation = event.getInteractionPoint();
        placeLocation.add(event.getBlockFace().getDirection());

        AxolotlBombEntity bomb = new AxolotlBombEntity(placeLocation);
        ((CraftWorld) event.getPlayer().getWorld()).getHandle().addFreshEntity(bomb);*/
    }

    @Override
    public ItemStack createCustomItem() {
        final ItemStack item = new ItemStack(Material.AXOLOTL_SPAWN_EGG, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(ChatColor.RED.toString() + ChatColor.BOLD + "Axolotl Bomb Spawn Egg"));
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        item.setItemMeta(meta);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        var itemNBT = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = (itemNBT.hasTag()) ? itemNBT.getTag() : new CompoundTag();
        if(itemTag != null) {
            itemTag.put("itemtype", StringTag.valueOf("axolotlbombspawn"));

            // Create the tag for the entity's type
            CompoundTag data = new CompoundTag();
            ListTag entityTags = new ListTag();
            entityTags.add(StringTag.valueOf("AxolotlBomb"));
            data.put("Tags", entityTags);
            itemTag.put("EntityTag", data);

            // Add the compound tag to the item NBT
            itemNBT.setTag(itemTag);
        }

        return CraftItemStack.asBukkitCopy(itemNBT);
    }
}
