package com.kagenyx.teamsplugin.listeners;

import com.kagenyx.teamsplugin.TeamRanksManager;
import com.kagenyx.teamsplugin.TeamsPlugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class ArcanaListener implements Listener {

    private TeamsPlugin plugin;

    public ArcanaListener(TeamsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEatEvent(PlayerItemConsumeEvent e) {
        if(plugin.getTrm().getPlayerTeam(e.getPlayer().getUniqueId()).equals("Arcana")) {
            if(checkMagicalFood(e.getItem().getType())) {
                plugin.setPlayerMagicFood(e.getPlayer());
            }
        }
    }

    private boolean checkMagicalFood(Material m){
        return m == Material.GOLDEN_APPLE
                || m == Material.GOLDEN_CARROT
                || m == Material.CHORUS_FRUIT
                || m == Material.GLOW_BERRIES
                || m == Material.ENCHANTED_GOLDEN_APPLE;
    }
}
