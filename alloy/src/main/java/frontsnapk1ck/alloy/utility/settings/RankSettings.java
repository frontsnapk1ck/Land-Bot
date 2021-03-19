package frontsnapk1ck.alloy.utility.settings;

public class RankSettings extends AbstractSettings {
    
    private int level;
    private int totalXP;
    private int xp;

    public RankSettings() 
    {
        this.level = 0;
        this.totalXP = 0;
        this.xp = 0;
    }

    public int getLevel() {
        return level;
    }

    public int getTotalXP() {
        return totalXP;
    }

    public int getXp() {
        return xp;
    }

    public RankSettings setLevel(int level) 
    {
        this.level = level;
        return this;
    }

    public RankSettings setTotalXP(int totalXP) 
    {
        this.totalXP = totalXP;
        return this;
    }

    public RankSettings setXp(int xp) 
    {
        this.xp = xp;
        return this;
    }

    public RankSettings copy()
    {
        RankSettings settings = new RankSettings();
        settings.setLevel(level)
                .setTotalXP(totalXP)
                .setXp(xp);

        return settings;
    }

}
