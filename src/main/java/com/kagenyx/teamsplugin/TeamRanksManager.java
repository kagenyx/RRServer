package com.kagenyx.teamsplugin;

import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class TeamRanksManager {

    private File file;
    private YamlConfiguration yaml;

    public TeamRanksManager(TeamsPlugin main) {
        if(!main.getDataFolder().exists()) {
            main.getDataFolder().mkdir();
        }

        file = new File(main.getDataFolder(),"TeamsRank.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public void setRank(UUID uuid, Rank rank) {
        config.set()
    }

    public void getRank() {

    }

}
