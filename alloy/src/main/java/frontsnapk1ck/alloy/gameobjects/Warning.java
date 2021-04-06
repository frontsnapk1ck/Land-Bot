package frontsnapk1ck.alloy.gameobjects;

import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.utility.settings.WarningSettings;
import frontsnapk1ck.io.Saver;

public class Warning extends GameObject {

    public static final String REASON = "reason";
    public static final String ISSUER = "issuer";
    public static final String ID = "id";
    public static final String TARGET = "target";

    private WarningSettings settings;

    public Warning(WarningSettings settings) 
    {
        this.settings = settings;
        save();
    }

    @Override
    protected void save() 
    {
        String[] out = new String[]
        {
            REASON  + ":" + this.settings.getReason() , 
            ISSUER  + ":" + this.settings.getIssuer() ,
            ID      + ":" + this.settings.getID() ,
            TARGET  + ":" + this.settings.getTarget() ,
        };
        try {
            Saver.saveOverwrite(this.settings.getPath(), out);
        } catch (Exception e) 
        {
            Alloy.LOGGER.warn("Warning", e.getMessage());
        }
    }

    public String getId() 
    {
		return this.settings.getID();
    }
    
    public String getReason()
    {
        return this.settings.getReason();
    }

    public String getIssuer()
    {
        return "<@!" + this.settings.getIssuer() + ">";
    }

    public String getTarget()
    {
        return "<@!" + this.settings.getTarget() + ">";
    }

    @Override
    public GameObject copy() 
    {
        return new Warning(this.settings.copy());
    }

    public Long getTargetLong() 
    {
		return this.settings.getIssuer();
	}

    @Override
    public GameObject getData() 
    {
        return this;
    }
    
}
