package landbot.gameobjects.player;

public class Rank {

    public static final String XP = "XP";
    public static final String LEVEL = "level";
    
    private int level;
    private int totalXP;
    private int xp;

    public Rank( int level , int xp , int totalXP )
    {
        this.level = level;
        this.xp = xp;
        this.totalXP = totalXP;
    }

    public int getLevel() 
    {
		return this.level;
	}

    public int getXP() 
    {
		return this.xp;
	}
    
    public int getTotalXP() {
        return totalXP;
    }
}
