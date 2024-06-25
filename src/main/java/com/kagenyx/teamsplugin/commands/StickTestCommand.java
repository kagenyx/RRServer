package com.kagenyx.teamsplugin.commands;

import com.kagenyx.teamsplugin.TeamRanksManager;
import com.kagenyx.teamsplugin.TeamsPlugin;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class StickTestCommand implements CommandExecutor {

    TeamsPlugin plugin;
    TeamRanksManager trm;

    public StickTestCommand(TeamRanksManager trm, TeamsPlugin plugin) {
        this.plugin = plugin;
        this.trm = trm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            ItemStack stick = new ItemStack(Material.STICK);
            ItemMeta meta = stick.getItemMeta();
            if (meta != null) {
                PersistentDataContainer data = meta.getPersistentDataContainer();
                data.set(plugin.getCustomKey(), PersistentDataType.STRING, "TribunalStick");
                meta.setDisplayName("Tribunal Stick");
                stick.setItemMeta(meta);
            }

            player.getInventory().addItem(stick);
            player.sendMessage("Você recebeu o Tribunal Stick!");

            return true;
        } else {
            sender.sendMessage("Este comando só pode ser executado por um jogador.");
            return false;
        }
    }
}
