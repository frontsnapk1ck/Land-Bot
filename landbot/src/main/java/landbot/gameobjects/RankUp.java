package landbot.gameobjects;

public class RankUp implements Comparable<RankUp> {

    public static final String USER = "{user}";
    public static final String USER_PING = "{user.ping}";
    public static final String ROLE = "{role}";
    public static final String LEVEL = "{level}";

    private int level;
    private long id;
    private String message;

    public RankUp(int level , long id , String message) 
    {
        this.id = id;
        this.level = level;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        String out = "";
        out += "level `" + level + "`\n";
        if ( this.id != 0l )
            out += "Gives role <@&" + this.id + ">\n";
        out += this.message;

        return out;
    }

    public String toSave() 
    {
        String out = "";
        out += this.level +":";
        if (this.id != 0l)
            out += this.id;
        out += ":";
        out += this.message;

        return out;
	}

    @Override
    public int compareTo(RankUp o) 
    {
        if (o.getLevel() > this.level)
            return -1;
        else if (o.getLevel() < this.getLevel())
            return 1;
        return 0;
    }
    
}
