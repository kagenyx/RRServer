package com.kagenyx.teamsplugin;

import com.google.common.math.BigIntegerMath;
import com.kagenyx.teamsplugin.commands.RankCommand;
import com.kagenyx.teamsplugin.listeners.KeepItemListener;
import com.kagenyx.teamsplugin.listeners.RightClickListener;
import com.kagenyx.teamsplugin.listeners.TribunalListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.LinkedList;
import java.util.UUID;

public final class TeamsPlugin extends JavaPlugin {

    private TeamRanksManager trm;
    @Override
    public void onEnable() {
        getCommand("rankup").setExecutor(new RankCommand());
        trm = new TeamRanksManager(this);
        Bukkit.getPluginManager().registerEvents(new TribunalListener(trm),this);
        Bukkit.getPluginManager().registerEvents(new KeepItemListener(),this);
        Bukkit.getPluginManager().registerEvents(new RightClickListener(this),this);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            //Tribunal
            LinkedList<UUID> tribunal_uuids = trm.extractUUIDs("Tribunal");
            for (UUID uuid : tribunal_uuids) {
                if(!trm.getTeamLeader("Tribunal").equals(uuid)) {
                    getHasteAmp(Bukkit.getPlayer(uuid).getLocation().getY(), Bukkit.getPlayer(uuid));
                } else {
                    Bukkit.getPlayer(uuid).addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,40,-1 + trm.getTeamRank("Tribunal")));
                }
            }
        },20,40);
    }

    public TeamRanksManager getTrm() {
        return this.trm;
    }

    private void getHasteAmp(Double y, Player p) {
        switch(trm.getTeamRank("Tribunal")) {
            case 1:
                if(y.intValue() >= -20 && y.intValue() <= 20) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,40,0));
                }
                break;
            case 2:
                if(y.intValue() >= -40 && y.intValue() <= 40) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,40,0));
                }
                break;
            case 3:
                p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,40,1));
                break;
            default:
                break;
        }
    }
}
