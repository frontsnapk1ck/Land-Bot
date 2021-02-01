package botcord.event;

public class SwitchTarget {

	public static final SwitchTarget     GUILD   = new SwitchTarget("guild");
	public static final SwitchTarget     DEBUG   = new SwitchTarget("debug");
	public static final SwitchTarget     PM      = new SwitchTarget("pm");
    
	public static final SwitchTarget     CHANNEL = new SwitchTarget("channel");
    
    private String id;
    
    public SwitchTarget(String id) 
    {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object obj) 
    {
        if (obj instanceof SwitchTarget )
            return ((SwitchTarget)obj).getId().equalsIgnoreCase(this.id);
        return false;
    }

    public String getId() {
        return id;
    }
}
