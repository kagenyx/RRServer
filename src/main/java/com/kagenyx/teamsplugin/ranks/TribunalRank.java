package com.kagenyx.teamsplugin.ranks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

public enum TribunalRank {

    TRIBUNAL1(Component.text("Tribunal").color(NamedTextColor.LIGHT_PURPLE)),
    TRIBUNAL2(Component.text("Tribunal").color(NamedTextColor.LIGHT_PURPLE)),
    TRIBUNAL3(Component.text("Tribunal").color(NamedTextColor.LIGHT_PURPLE));

    private TextComponent display;

    TribunalRank(TextComponent display) {
        this.display = display;
    }

    public TextComponent getDisplay() {
        return this.display;
    }
}
