package com.kagenyx.teamsplugin;

import com.kagenyx.teamsplugin.commands.RankCommand;
import com.kagenyx.teamsplugin.listeners.TribunalListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TeamsPlugin extends JavaPlugin {

    private TeamRanksManager trm;
    @Override
    public void onEnable() {
        getCommand("rankup").setExecutor(new RankCommand());
        trm = new TeamRanksManager(this);
        Bukkit.getPluginManager().registerEvents(new TribunalListener(trm),this);
    }

    public TeamRanksManager getTrm() {
        return this.trm;
    }
}
