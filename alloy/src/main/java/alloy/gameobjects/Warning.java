package alloy.gameobjects;

import alloy.utility.settings.WarningSettings;
import io.Saver;

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
        Saver.saveOverwite(this.settings.getPath(), out);
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
