package frontsnapk1ck.alloy.utility.settings;

public class RankUpSettings extends AbstractSettings {

    private int level;
    private long id;
    private String message;


    @Override
    public RankUpSettings copy() 
    {
        RankUpSettings settings = new RankUpSettings();
        settings.setId(id)
                .setLevel(level)
                .setMessage(message);

        return settings;
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

    public RankUpSettings setId(long id) 
    {
        this.id = id;
        return this;
    }

    public RankUpSettings setLevel(int level) 
    {
        this.level = level;
        return this;
    }

    public RankUpSettings setMessage(String message) 
    {
        this.message = message;
        return this;
    }

    
}
