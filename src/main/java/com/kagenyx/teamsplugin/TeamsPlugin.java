package com.kagenyx.teamsplugin;

import com.kagenyx.teamsplugin.commands.RankCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class TeamsPlugin extends JavaPlugin {

    private TeamRanksManager trm;
    @Override
    public void onEnable() {
        getCommand("rank").setExecutor(new RankCommand());
        trm = new TeamRanksManager(this);
    }

    public TeamRanksManager getTrm() {
        return this.trm;
    }
}
