package com.kagenyx.teamsplugin.commands;

import com.kagenyx.teamsplugin.TeamRanksManager;
import com.kagenyx.teamsplugin.TeamsPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class RankCostCommand implements CommandExecutor {

    private final TeamsPlugin plugin;
    private final static Double RANKUP_1_COST = 100000.00;
    private final static Double RANKUP_2_COST = 800000.00;

    public RankCostCommand(TeamsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player p = (Player) sender;

        switch (plugin.getTrm().getTeamRank(plugin.getTrm().getPlayerTeam(p.getUniqueId()))){
            case 1:
                p.sendMessage("The next update for your team costs " + RANKUP_1_COST.intValue() + "€") ;
                break;
            case 2:
                p.sendMessage("The next update for your team costs " + RANKUP_2_COST.intValue() + "€") ;
                break;
            default:
                p.sendMessage("There's no more upgrades for your team! You are very strong!");
        }

        return true;
    }
}
