package com.aasmart.PluginExperiment.handlers;

import com.aasmart.PluginExperiment.Main;
import com.aasmart.PluginExperiment.entities.types.AxolotlBombEntity;
import com.aasmart.PluginExperiment.entities.EntityUtils;
import com.aasmart.PluginExperiment.entities.ICustomEntity;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;

public class EntitySpawnHandler implements Listener {
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
            LivingEntity entity = event.getEntity();

            if(entity.getScoreboardTags().contains("AxolotlBomb"))
                EntityUtils.spawn(AxolotlBombEntity.class, event.getEntity().getLocation(), event.getEntity().getWorld(), event.getEntity());
        }
    }

    @EventHandler
    public void onEntityAddToWorld(EntityAddToWorldEvent event) {
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "CustomMobType");
        String mobType = event.getEntity().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        Class<? extends ICustomEntity> entityClass = EntityUtils.getRegisteredEntity(mobType);
        if(entityClass != null)
            EntityUtils.spawn(entityClass, event.getEntity().getLocation(), event.getEntity().getWorld(), event.getEntity());
    }
}
