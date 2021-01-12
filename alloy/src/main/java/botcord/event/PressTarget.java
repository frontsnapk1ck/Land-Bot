package botcord.event;

public class PressTarget {

	public static final PressTarget     GUILD   = new PressTarget("guild");
	public static final PressTarget     DEBUG   = new PressTarget("debug");
	public static final PressTarget     PM      = new PressTarget("pm");
    
    
    private String id;
    
    public PressTarget(String id) 
    {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object obj) 
    {
        if (obj instanceof PressTarget )
            return ((PressTarget)obj).getId().equalsIgnoreCase(this.id);
        return false;
    }

    public String getId() {
        return id;
    }
}
