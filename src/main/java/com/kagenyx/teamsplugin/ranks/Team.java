package com.kagenyx.teamsplugin.ranks;

import com.kagenyx.teamsplugin.TeamsPlugin;
import net.kyori.adventure.text.TextComponent;

import java.util.LinkedList;
import java.util.UUID;

public abstract class Team {
    protected int level;
    protected TextComponent display;
    protected LinkedList<UUID> players;

    public Team(TextComponent display, TeamsPlugin main) {
        this.display = display;
    }

    public void setLevel(int i) {
        this.level = i;
    }



}
