package com.kagenyx.teamsplugin;

import com.kagenyx.teamsplugin.ranks.enums.TribunalRank;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

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

    public void setTribunalRank(UUID uuid, TribunalRank rank) {
        yaml.set(uuid.toString(),rank.name());
    }

    public TribunalRank getRank(UUID uuid) {
        return TribunalRank.valueOf(yaml.getString(uuid.toString()));
    }

    public int getTribunalRank(){
        return yaml.getInt("Tribunal");
    }

    public int getArcanaRank(){
        return yaml.getInt("Arcana");
    }

    public int getKarmaRank(){
        return yaml.getInt("Karma");
    }

}
