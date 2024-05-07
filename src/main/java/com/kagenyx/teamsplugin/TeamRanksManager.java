package com.kagenyx.teamsplugin;

import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TeamRanksManager {

    private File file;
    private JSONObject json;

    public TeamRanksManager(TeamsPlugin main) {
        if (!main.getDataFolder().exists()) {
            main.getDataFolder().mkdir();
        }

        file = new File(main.getDataFolder(), "TeamsRank.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
                // Initialize with default values
                JSONObject defaultRanks = new JSONObject();
                defaultRanks.put("TribunalRank", 0);
                defaultRanks.put("ArcanaRank", 0);
                defaultRanks.put("KarmaRank", 0);
                FileWriter writer = new FileWriter(file);
                writer.write(defaultRanks.toJSONString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader(file);
            json = (JSONObject) parser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public int getTeamRank(String teamName) {
        return ((Long) json.getOrDefault(teamName + "Rank", 0L)).intValue();
    }

    public void setTeamRank(String teamName, int rank) {
        json.put(teamName + "Rank", rank);
        saveToFile();
    }

    private void saveToFile() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
