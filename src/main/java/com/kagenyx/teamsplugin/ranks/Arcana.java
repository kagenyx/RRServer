package com.kagenyx.teamsplugin.ranks;

import com.kagenyx.teamsplugin.TeamsPlugin;
import net.kyori.adventure.text.TextComponent;

public class Arcana extends Team{
    public Arcana(TextComponent display, TeamsPlugin main) {
        super(display, main);
        this.level = main.getTrm().getArcanaRank();
    }
}
