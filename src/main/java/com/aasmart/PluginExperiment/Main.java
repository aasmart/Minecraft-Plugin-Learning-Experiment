package com.aasmart.PluginExperiment;

import com.aasmart.PluginExperiment.InteractableItems.Items.IceSpikeStaff;
import com.aasmart.PluginExperiment.commands.PetsCommand;
import com.aasmart.PluginExperiment.entities.EntityUtils;
import com.aasmart.PluginExperiment.entities.types.AxolotlBombEntity;
import com.aasmart.PluginExperiment.handlers.EntitySpawnHandler;
import com.aasmart.PluginExperiment.handlers.ItemInteractHandler;
import com.aasmart.PluginExperiment.handlers.JoinHandler;
import com.aasmart.PluginExperiment.InteractableItems.InteractableItem;
import com.aasmart.PluginExperiment.InteractableItems.Items.AxolotlBombSpawn;
import com.aasmart.PluginExperiment.InteractableItems.Items.FlameStick;
import com.aasmart.PluginExperiment.commands.AxolotlBombCommand;
import com.aasmart.PluginExperiment.commands.ItemsGuiCommand;
import com.aasmart.PluginExperiment.guis.GUI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public final class Main extends JavaPlugin {
    public static Server server;

    @Override
    @SuppressWarnings("all")
    public void onEnable() {
        GUI.initialize(getServer().getPluginManager(), this);

        // Plugin startup logic
        getLogger().info(ChatColor.GREEN + "Enabled " + this.getName());
        getServer().getPluginManager()
                .registerEvents(new JoinHandler(), this);
        getServer().getPluginManager()
                .registerEvents(new ItemInteractHandler(), this);
        getServer().getPluginManager()
                .registerEvents(new EntitySpawnHandler(), this);
        server = getServer();

        this.getCommand("axolotlbomb").setExecutor(new AxolotlBombCommand());
        this.getCommand("customitems").setExecutor(new ItemsGuiCommand());
        this.getCommand("pets").setExecutor(new PetsCommand());

        EntityUtils.registerEntity(AxolotlBombEntity.class);
    }

    @Override
    public void onLoad() {
        InteractableItem.addItem(new FlameStick(true, false));
        InteractableItem.addItem(new AxolotlBombSpawn(false, true));
        InteractableItem.addItem(new IceSpikeStaff(false, true));
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "Disabled " + this.getName());
    }

    public static class Utils {
        public static Vector randomVector(double scalar) {
            double angle = Math.toRadians(Math.random() * 360);

            double z = (Math.random() * 2) - 1;
            double x = Math.sqrt(1 - (z * z)) * Math.cos(angle);
            double y = Math.sqrt(1 - (z * z)) * Math.sin(angle);

            return new Vector(x, y, z).multiply(scalar);
        }

        public static ArrayList<Block> sphere(final Location center, final int radius) {
            ArrayList<Block> sphere = new ArrayList<>();
            for (int Y = -radius; Y < radius; Y++)
                for (int X = -radius; X < radius; X++)
                    for (int Z = -radius; Z < radius; Z++)
                        if (Math.sqrt((X * X) + (Y * Y) + (Z * Z)) <= radius) {
                            final Block block = center.getWorld().getBlockAt(X + center.getBlockX(), Y + center.getBlockY(), Z + center.getBlockZ());
                            sphere.add(block);
                        }
            return sphere;
        }

        public static ArrayList<Block> circle(final Location center, final int radius) {
            ArrayList<Block> circle = new ArrayList<>();
            for (int X = -radius; X <= radius; X++)
                for (int Z = -radius; Z <= radius; Z++)
                    if (Math.sqrt((X * X) + (Z * Z)) <= radius) {
                        final Block block = center.getWorld().getBlockAt(X + center.getBlockX(), center.getBlockY(), Z + center.getBlockZ());
                        circle.add(block);
                    }
            return circle;
        }
    }
}
