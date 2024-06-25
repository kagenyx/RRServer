package com.kagenyx.teamsplugin.commands;

import com.kagenyx.teamsplugin.TeamsPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeamJoinCommand implements CommandExecutor {
    TeamsPlugin plugin;

    public TeamJoinCommand(TeamsPlugin teamsPlugin) {
        plugin = teamsPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player) {
            if(strings.length == 1) {
                Player player = (Player) commandSender;
                try {
                    if(this.plugin.getTrm().getPlayerTeam(player.getUniqueId()) == null) {
                        this.plugin.getTrm().addUUIDToTeams(player.getUniqueId().toString(),strings[0]);
                        player.sendMessage(Component.text("Estás agora na equipa " +  strings[0] + "! Boa sorte na tua aventura.").color(NamedTextColor.GREEN));
                    } else {
                        player.sendMessage(Component.text("Já estás numa equipa. Para mudar contacta um administrador.").color(NamedTextColor.RED));
                    }
                } catch (Exception e){
                    player.sendMessage(Component.text("Equipa errada. Por favor escreve /jointeam Arcana ou /jointeam Tribunal").color(NamedTextColor.RED));
                }
            }
        }
        return true;
    }
}
