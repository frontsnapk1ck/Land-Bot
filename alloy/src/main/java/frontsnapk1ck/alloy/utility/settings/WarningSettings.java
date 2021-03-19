package frontsnapk1ck.alloy.utility.settings;

public class WarningSettings extends AbstractSettings {

    private String reason;
    private long issuer;
    private String id;
    private long target;

    public WarningSettings() 
    {
        this.reason = "No Reason Provided";
        this.issuer = 0l;
        this.id = "";
        setPath( "" );
    }

    public String getReason() 
    {
		return reason;
	}

    public Long getIssuer() 
    {
		return issuer;
	}

    public String getID() 
    {
		return id;
    }
    
    public WarningSettings setId(String id) 
    {
        this.id = id;
        return this;
    }

    public WarningSettings setIssuer(long issuer) 
    {
        this.issuer = issuer;
        return this;
    }

    public WarningSettings setReason(String reason) 
    {
        this.reason = reason;
        return this;
    }

    @Override
    public WarningSettings setPath(String path) 
    {
        super.setPath(path);
        return this;
    }

    public WarningSettings setTarget(long target) 
    {
        this.target = target;
        return this;
    }

    public WarningSettings copy()
    {
        WarningSettings settings = new WarningSettings();
        settings.setId(id)
                .setIssuer(issuer)
                .setPath(getPath())
                .setTarget(target)
                .setReason(reason);
        
        return settings;
    }

    public long getTarget() 
    {
		return this.target;
	}
    
}
