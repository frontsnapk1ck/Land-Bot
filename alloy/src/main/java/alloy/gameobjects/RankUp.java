package alloy.gameobjects;

import alloy.utility.settings.RankUpSettings;

public class RankUp implements Comparable<RankUp> {

    public static final String USER = "{user}";
    public static final String USER_PING = "{user.ping}";
    public static final String ROLE = "{role}";
    public static final String LEVEL = "{level}";
   
    private RankUpSettings settings;

    public RankUp( RankUpSettings settings ) 
    {
        this.settings = settings;
    }

    public long getId() 
    {
        return this.settings.getId();
    }

    public int getLevel() {
        return this.settings.getLevel();
    }

    public String getMessage() 
    {
        return this.settings.getMessage();
    }

    @Override
    public String toString() {
        String out = "";
        out += "level `" + this.getLevel() + "`\n";
        if ( this.getId() != 0l )
            out += "Gives role <@&" + this.getId()+ ">\n";
        out += this.getMessage();

        return out;
    }

    public String toSave() 
    {
        String out = "";
        out += this.getLevel() +":";
        if (this.getId() != 0l)
            out += this.getId();
        out += ":";
        out += this.getMessage();

        return out;
	}

    @Override
    public int compareTo(RankUp o) 
    {
        if (o.getLevel() > this.getLevel())
            return -1;
        else if (o.getLevel() < this.getLevel())
            return 1;
        return 0;
    }
    
}
