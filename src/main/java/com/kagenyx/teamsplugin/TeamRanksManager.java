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

        file = new File(main.getDataFolder(), "Teams.json");
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
            reader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public int getTeamRank(String teamName) {
        Object value = json.get(teamName + "Rank");
        if (value instanceof Long) {
            return ((Long) value).intValue();
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else {
            System.out.println("Invalid rank value! Returning default value");
            return 1;
        }
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
            FileReader reader = new FileReader(file); // Use the correct file path
            JSONObject jsonData = (JSONObject) parser.parse(reader);
            reader.close();

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

    public UUID getTeamLeader(String teamName) {
        String uuidString = (String) json.get(teamName + "Leader");
        return UUID.fromString(uuidString);
    }

    public String getPlayerTeam(UUID uuid) {
        String[] arr = {"Arcana","Tribunal"};
        for (Object key : arr) {
            String teamName = (String) key;
            Object teamMembersObj = json.get(teamName);
            if (teamMembersObj instanceof JSONArray) {
                JSONArray teamMembers = (JSONArray) teamMembersObj;
                for (Object member : teamMembers) {
                    if (member.toString().equals(uuid.toString())) {
                        return teamName;
                    }
                }
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
