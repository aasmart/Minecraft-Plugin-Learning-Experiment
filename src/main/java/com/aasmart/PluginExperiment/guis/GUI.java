package com.aasmart.PluginExperiment.guis;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.LinkedHashMap;

public class GUI {
    public static LinkedHashMap<String, GUI> guis;

    public static void initialize(PluginManager manager, Plugin plugin) {
        guis = new LinkedHashMap<>();

        guis.put("customitems", CustomItemsGui.initializeGUI(manager, plugin));
    }
}
