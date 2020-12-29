package alloy.command.util;

import java.awt.Color;

public enum PunishType {
    WARN        (1, "Warn", "warned", "Adds a strike to the user", new Color(0xA8CF00)),
    MUTE        (2, "Mute", "muted", "Adds the configured muted role to user", new Color(0xFFF300)),
    KICK        (3, "Kick", "kicked", "Remove user from the guild", new Color(0xFF9600)),
    TMP_BAN     (4, "Temp-ban", "temporarily banned", "Remove user from guild, unable to rejoin for a while", new Color(0xFF4700)),
    BAN         (5, "Ban", "banned", "Permanently removes user from guild", new Color(0xB70000)), 
    UNMUTE      (6, "Unmute" , "unmuted" , "Removes the configured muted role to user" , new Color(0x686300) ),;

    private final int id;
    private final String keyword;
    private final String verb;
    private final String description;
    private final Color color;

    PunishType(int id, String keyword, String verb, String description, Color color) {
        this.id = id;
        this.keyword = keyword;
        this.description = description;
        this.color = color;
        this.verb = verb;
    }

    public static PunishType fromId(int id) {
        for (PunishType et : values()) {
            if (id == et.getId()) {
                return et;
            }
        }
        return KICK;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getKeyword() {
        return keyword;
    }

    public Color getColor() {
        return color;
    }

    public String getVerb() {
        return verb;
    }

    public static PunishType parseType(String s) 
    {
        for (PunishType p : values()) 
        {
            if (p.getKeyword().equalsIgnoreCase(s))
                return p;
        }
        return null;
	}
}
