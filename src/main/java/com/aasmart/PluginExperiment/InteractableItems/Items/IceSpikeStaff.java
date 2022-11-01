package com.aasmart.PluginExperiment.InteractableItems.Items;

import com.aasmart.PluginExperiment.InteractableItems.InteractableItem;
import com.aasmart.PluginExperiment.Main;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class IceSpikeStaff extends InteractableItem {
    public IceSpikeStaff(boolean hasLeftFunction, boolean hasRightFunction) {
        super(hasLeftFunction, hasRightFunction);
    }

    @Override
    public boolean canUseItem(PlayerInteractEvent event) {
        if(event.getItem() == null)
            return false;

        ItemStack eventItem = event.getItem();
        return eventItem.getType() == getCustomItem().getType()
                && ItemUtils.hasTag(eventItem, "itemtype", "IceSpikeStaff");
    }

    @Override
    public void leftUse(PlayerInteractEvent event) {

    }

    @Override
    public void rightUse(PlayerInteractEvent event) {
        event.setCancelled(true);

        Block block = event.getPlayer().getTargetBlock(30);
        if(block == null || block.getType() == Material.AIR)
            return;


        Location location = block.getLocation().add(0, -2, 0);

        final int[] iterations = {1};
        final int totalIterations = (int)(Math.random() * 20) + 20;
        final int shift = (int)(totalIterations/2.0);
        BukkitTask projectileTask = new BukkitRunnable() {
            @Override
            public void run() {
                if(iterations[0] >= totalIterations)
                    return;
                int radius = (int)(Math.pow(.88, iterations[0]-shift)+.75);
                Main.Utils.circle(location.add((Math.random() * 2) - 1, 0, (Math.random() * 2) - 1), radius)
                        .forEach(block -> block.setType(Material.ICE));
                location.add(new Vector(0, 1, 0));
                location.getNearbyLivingEntities(radius, 1)
                        .forEach(entity -> entity.setVelocity(entity.getVelocity().add(new Vector(0, 1, 0))));
                location.getWorld().playSound(location, Sound.BLOCK_GLASS_BREAK, 5, 1);

                iterations[0]++;
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 1L);
    }

    @Override
    public ItemStack createCustomItem() {
        final ItemStack item = new ItemStack(Material.ICE, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(ChatColor.BLUE.toString() + ChatColor.BOLD + "Frozen Spike"));
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        item.setItemMeta(meta);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        var itemNBT = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = (itemNBT.hasTag()) ? itemNBT.getTag() : new CompoundTag();
        if(itemTag != null) {
            itemTag.put("itemtype", StringTag.valueOf("IceSpikeStaff"));
            itemNBT.setTag(itemTag);
        }

        return CraftItemStack.asBukkitCopy(itemNBT);
    }
}
