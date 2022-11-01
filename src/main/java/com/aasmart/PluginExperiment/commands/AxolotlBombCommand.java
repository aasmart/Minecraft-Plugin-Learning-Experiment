package com.aasmart.PluginExperiment.commands;

import com.aasmart.PluginExperiment.entities.types.AxolotlBombEntity;
import com.aasmart.PluginExperiment.entities.EntityUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AxolotlBombCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            EntityUtils.spawn(AxolotlBombEntity.class, player.getLocation(), player.getWorld());
        }
        return true;
    }
}
