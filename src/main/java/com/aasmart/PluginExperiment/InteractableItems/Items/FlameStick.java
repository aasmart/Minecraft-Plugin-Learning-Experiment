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
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class FlameStick extends InteractableItem {
    public FlameStick(boolean hasLeftFunction, boolean hasRightFunction) {
        super(hasLeftFunction, hasRightFunction);
    }

    public boolean canUseItem(PlayerInteractEvent event) {
        if(event.getItem() == null)
            return false;

        ItemStack eventItem = event.getItem();
        return eventItem.getType() == getCustomItem().getType()
                && ItemUtils.hasTag(eventItem, "itemtype", "infernolash");
    }

    public void leftUse(PlayerInteractEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
        Vector dir = player.getEyeLocation().getDirection().multiply(1.5);

        Location projectilePos = player.getEyeLocation();
        ArmorStand projectile = spawnProjectile(projectilePos, player);

        BukkitTask projectileTask = new BukkitRunnable() {
            @Override
            public void run() {
                player.getWorld().spawnParticle(Particle.FLAME, projectile.getLocation().toVector().add(new Vector(0, 2, 0)).toLocation(player.getWorld()), 5, 0, 0, 0, .03);
                projectilePos.add(dir);
                projectile.teleport(projectilePos.toVector().add(new Vector(0, -2, 0)).toLocation(player.getWorld()));
                projectile.setRotation(projectilePos.getYaw(), projectilePos.getPitch());

                Material blockType = projectilePos.getBlock().getType();
                if(blockType == Material.WATER) {
                    projectile.remove();
                    this.cancel();
                    player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, projectilePos, 150, 0, 0, 0, .05);
                    player.getWorld().spawnParticle(Particle.WATER_SPLASH, projectilePos, 150, 1, 0, 1, 1);
                    player.getWorld().playSound(projectilePos, Sound.BLOCK_LAVA_EXTINGUISH, 10, 1);
                    player.getWorld().playSound(projectilePos, Sound.ENTITY_PLAYER_SPLASH, 10, 1);

                } else if((blockType != Material.AIR && blockType != Material.CAVE_AIR && blockType != Material.VOID_AIR)
                        || (projectilePos.getNearbyLivingEntities(1, livingEntity -> livingEntity != player
                        && livingEntity != projectile && !(livingEntity.hasMetadata("owner")
                        && livingEntity.getMetadata("owner").get(0).value() == player)).size() > 0)
                ) {
                    this.cancel();
                    explode(projectilePos, player, projectile);
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 1L);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!projectileTask.isCancelled()) {
                    projectileTask.cancel();
                    explode(projectilePos, player, projectile);
                }
            }
        }.runTaskLater(Main.getPlugin(Main.class), 40L);
    }

    @Override
    public void rightUse(PlayerInteractEvent event) {

    }

    @Override
    public ItemStack createCustomItem() {
        final ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(ChatColor.RED.toString() + ChatColor.BOLD + "Infernolash"));
        meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        item.setItemMeta(meta);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        var itemNBT = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = (itemNBT.hasTag()) ? itemNBT.getTag() : new CompoundTag();
        if(itemTag != null) {
            itemTag.put("itemtype", StringTag.valueOf("infernolash"));
            itemNBT.setTag(itemTag);
        }

        return CraftItemStack.asBukkitCopy(itemNBT);
    }

    private ArmorStand spawnProjectile(Location projectilePos, Player player) {
        ArmorStand projectile = (ArmorStand) player.getWorld().spawnEntity(projectilePos.toVector().add(new Vector(0, 100, 0)).toLocation(player.getWorld()), EntityType.ARMOR_STAND);
        projectile.setVisible(false);
        projectile.setMetadata("owner", new FixedMetadataValue(Main.getPlugin(Main.class), player));
        projectile.teleport(projectilePos.toVector().add(new Vector(0, -2, 0)).toLocation(player.getWorld()));
        projectile.setRotation(projectilePos.getYaw(), projectilePos.getPitch());
        projectile.setItem(EquipmentSlot.HEAD, new ItemStack(Material.MAGMA_BLOCK));
        return projectile;
    }

    public void explode(Location projectilePos, Player player, Entity projectile) {
        projectile.remove();
        projectilePos.createExplosion(player, 10.0f, true, true);
        player.getWorld().spawnParticle(Particle.FLAME, projectilePos, 500, 0, 0, 0, .3);
        player.getWorld().spawnParticle(Particle.SMOKE_LARGE, projectilePos, 100, 0, 0, 0, .4);
        ArrayList<Block> blocks = Main.Utils.sphere(projectilePos, 8);

        for (Block b : blocks) {
            if ((b.getType() != Material.AIR
                    && b.getType() != Material.CAVE_AIR
                    && b.getType() != Material.VOID_AIR
                    && b.getType() != Material.FIRE
                    && b.getType() != Material.WATER
                    && b.getType() != Material.LAVA
                    && b.getType() != Material.BEDROCK)
                    && Math.random() < .65)
            {
                Material material;
                double rand = Math.random();
                if(rand < .4)
                    material = Material.NETHERRACK;
                else if(rand < .8)
                    material = Material.MAGMA_BLOCK;
                else
                    material = Material.SMOOTH_BASALT;

                b.setType(material);
            }
        }
    }
}
