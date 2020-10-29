package landbot.gameobjects.player;

import landbot.gameobjects.GameObject;
import landbot.io.Saver;

public class Account extends GameObject {

    public static final String BAL = "bal";

    public int bal;
    private String path;

    public Account(int bal , String path) 
    {
        this.bal = bal;
        this.path = path;
    }

    @Override
    protected void save() 
    {
        String[] out = {
            BAL + ">" + this.bal, 
        };

        Saver.saveOverwite(path, out);

    }

    public int getBal() 
    {
		return this.bal;
	}

    public void add(int bal2) 
    {
        this.bal += bal2;
	}

    public boolean buy(int cost) 
    {
        if (this.bal >= cost)
        {
            this.bal -= cost;
            return true;
        }
        return false;
	}
    
}
