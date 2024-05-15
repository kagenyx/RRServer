package com.kagenyx.teamsplugin;

import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;
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
                defaultRanks.put("TribunalRank", 1);
                defaultRanks.put("ArcanaRank", 1);
                defaultRanks.put("KarmaRank", 1);
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
        return ((Long) json.getOrDefault(teamName + "Rank", 1L)).intValue();
    }

    public void setTeamRank(String teamName, int rank) {
        json.put(teamName + "Rank", rank);
        saveToFile();
    }

    public LinkedList<UUID> extractUUIDs(String teamName) {
        LinkedList<UUID> uuids = new LinkedList<>();

        // Read the JSON file and parse it
        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader("teams.json"); // Replace "teams.json" with your actual file path
            JSONObject jsonData = (JSONObject) parser.parse(reader);

            // Check if the team name exists in the JSON data
            if (jsonData.containsKey(teamName)) {
                JSONArray uuidArray = (JSONArray) jsonData.get(teamName);
                for (Object uuidObj : uuidArray) {
                    String uuidString = (String) uuidObj;
                    uuids.add(UUID.fromString(uuidString));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return uuids;
    }


    public UUID getTeamLeader(String teamName) { return (UUID) json.get(teamName + "Leader"); }

    public String getPlayerTeam(UUID playerUUID) {
        for (Object key : json.keySet()) {
            String teamName = (String) key;
            JSONArray members = (JSONArray) json.get(teamName);
            if (members.contains(playerUUID.toString())) {
                return teamName;
            }
        }
        return null;
    }

    private void saveToFile() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
