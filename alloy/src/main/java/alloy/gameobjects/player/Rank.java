package alloy.gameobjects.player;

import alloy.utility.settings.RankSettings;

public class Rank {

    public static final String XP = "XP";
    public static final String LEVEL = "level";

    private RankSettings settings;

    public Rank( RankSettings settings )
    {
        this.settings = settings;
    }

    public int getLevel() 
    {
		return this.settings.getLevel();
	}

    public int getXP() 
    {
		return this.settings.getXp();
	}
    
    public int getTotalXP() {
        return this.settings.getTotalXP();
    }
}
