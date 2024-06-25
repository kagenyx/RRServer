package com.kagenyx.teamsplugin.commands;

import com.kagenyx.teamsplugin.TeamsPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CheckLevelCommand implements CommandExecutor {

    TeamsPlugin plugin;

    public CheckLevelCommand(TeamsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(this.plugin.getTrm().getPlayerTeam(player.getUniqueId()) != null) {
                player.sendMessage(Component.text("A tua Equipa" +
                                plugin.getTrm().getPlayerTeam(player.getUniqueId()) + "está no nível " + this.plugin.getTrm().getTeamRank(this.plugin.getTrm().getPlayerTeam(player.getUniqueId())))
                        .color(NamedTextColor.GREEN));
            } else {
                player.sendMessage(Component.text("Erro ao usar.").color(NamedTextColor.RED));
            }
        }
        return true;
    }
}
