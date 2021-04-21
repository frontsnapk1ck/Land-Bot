package frontsnapk1ck.alloy.gameobjects.player;

import frontsnapk1ck.alloy.gameobjects.GameObject;
import frontsnapk1ck.alloy.utility.settings.AccountSettings;
import frontsnapk1ck.io.Saver;

public class Account extends GameObject {

    public static final String BAL = "bal";

    private AccountSettings settings;

    public Account( AccountSettings settings)
    {
        this.settings = settings;
    }

    @Override
    protected void save() 
    {
        String[] out = {
            BAL + ">" + this.settings.getBal(), 
        };

        Saver.saveOverwrite(this.settings.getPath() , out);

    }

    public int getBal() 
    {
		return this.settings.getBal();
	}

    public void setBalance(int bal) 
    {
        this.settings.setBal(bal);
    }

    public void add(int bal) 
    {
        int newBal = this.settings.getBal() + bal;
        this.settings.setBal(newBal);
	}

    public boolean subtract(int cost) 
    {
        if (this.settings.getBal() >= cost)
        {
            int newBal = this.settings.getBal() - cost;
            this.settings.setBal(newBal);
            return true;
        }
        return false;
	}

    public GameObject copy() 
    {
        return new Account( this.settings.copy() );
    }

    @Override
    public GameObject getData() 
    {
        return this;
    }
    
}
