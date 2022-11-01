package com.aasmart.PluginExperiment.commands;

import com.aasmart.PluginExperiment.guis.CustomItemsGui;
import com.aasmart.PluginExperiment.guis.GUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ItemsGuiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            ((CustomItemsGui)GUI.guis.get(command.getName())).open(player);
        }
        return true;
    }
}
