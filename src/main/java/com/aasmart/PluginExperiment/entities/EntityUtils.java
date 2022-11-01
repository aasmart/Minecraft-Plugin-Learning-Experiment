package com.aasmart.PluginExperiment.entities;

import com.aasmart.PluginExperiment.Main;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.logging.Level;

public class EntityUtils {
    private static final LinkedHashMap<String, Class<? extends ICustomEntity>> customMobs = new LinkedHashMap<>();

    public static void spawn(Class<? extends ICustomEntity> type, Location location, World world) {
        try {
            NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "CustomMobType");
            LivingEntity entityNew =
                    (LivingEntity) type.getConstructor(Location.class).newInstance(location);
            ((CraftWorld) world).getHandle().addFreshEntity(entityNew);
            entityNew.getBukkitLivingEntity().getPersistentDataContainer()
                    .set(key, PersistentDataType.STRING, type.getDeclaredField("MOB_ID").get(null).toString());
        } catch (Exception e) {
            Main.getPlugin(Main.class).getLogger().info("Failed to spawn entity: " + e.getMessage());
        }
    }

    public static void spawn(Class<? extends ICustomEntity> type, Location location, World world, Entity entity) {
        spawn(type, location, world);
        entity.remove();
    }

    /**
     * Places an entity in the entity registry that can be accesssed using {@link #getRegisteredEntity(String)}
     *
     * @param entity The class of the entity to register.
     *               <p>The entity must have a <code>public static final String MOB_ID</code> field in order
     *               to be added to the registry</p>
     */
    public static void registerEntity(Class<? extends ICustomEntity> entity) {
        try {
            Field id = entity.getDeclaredField("MOB_ID");
            customMobs.put(id.get(null).toString(), entity);
        } catch (NoSuchFieldException e) {
            Main.getPlugin(Main.class)
                    .getLogger()
                    .log(Level.WARNING, "Failed to register entity of class " + entity.getName() + ": " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public static Class<? extends ICustomEntity> getRegisteredEntity(String id) {
        return customMobs.getOrDefault(id, null);
    }
}
