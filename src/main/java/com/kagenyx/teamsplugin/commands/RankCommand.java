package com.kagenyx.teamsplugin.commands;

import com.kagenyx.teamsplugin.TeamRanksManager;
import com.kagenyx.teamsplugin.TeamsPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class RankCommand implements CommandExecutor {

    private final TeamsPlugin plugin;
    private final static Double RANKUP_1_COST = 100000.00;
    private final static Double RANKUP_2_COST = 500000.00;

    public RankCommand(TeamsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        TeamRanksManager trm = plugin.getTrm();
        Player player = (Player) sender;
        String team = trm.getPlayerTeam(player.getUniqueId());
        Double rankUpCost = 100000000000000000.00;
        int lvlup = 1;

        switch (trm.getTeamRank(team)) {
            case 1:
                rankUpCost = RANKUP_1_COST;
                lvlup = 2;
                break;
            case 2:
                rankUpCost = RANKUP_2_COST;
                lvlup = 3;
                break;
            default:
                player.sendMessage("There was an error processing your request. Contact an Admin.");
                return false;
        }

        if (plugin.getEconomy().getBalance(player) >= rankUpCost) {
            plugin.getEconomy().withdrawPlayer(player, rankUpCost);
            player.sendMessage(Component.text("Your team has ranked up! Check /warp teams to see your rewards.").color(NamedTextColor.GREEN));
            trm.setTeamRank(team,lvlup);
        } else {
            player.sendMessage("You do not have enough money to rank up your team.");
        }

        return true;
    }
}
