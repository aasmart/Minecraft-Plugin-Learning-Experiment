package com.aasmart.PluginExperiment.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinHandler implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().chat("I LOVE THIS SERVER <3");
    }

    /*@EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        for(int i = 0; i < 100; i++) {
            TNTPrimed entity = (TNTPrimed) block.getWorld().spawnEntity(block.getLocation(), EntityType.PRIMED_TNT);
            entity.setFuseTicks(80);

            // Transform the vector to be only vertical
            Vector vector = Main.Utils.randomVector(1.5);
            vector.setY(Math.abs(vector.getY()));

            entity.setVelocity(vector);
        }
    }*/
}
