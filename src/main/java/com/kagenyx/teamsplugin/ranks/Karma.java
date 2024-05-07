package com.kagenyx.teamsplugin.ranks;

import com.kagenyx.teamsplugin.TeamsPlugin;
import net.kyori.adventure.text.TextComponent;

public class Karma extends Team{
    public Karma(TextComponent display, TeamsPlugin main) {
        super(display, main);
        this.level = main.getTrm().getKarmaRank();
    }
}
