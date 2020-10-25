package landbot.player;

import landbot.io.Saver;
import landbot.utility.GameObject;

public class Rank extends GameObject {

    public static final String XP = "XP";
    
    private String path;

    public Rank(String path)
    {
        this.path = path;
    }

    @Override
    protected void save() 
    {
        String[] out = {

        };

        Saver.saveOverwite(path, out);

    }
    
}
