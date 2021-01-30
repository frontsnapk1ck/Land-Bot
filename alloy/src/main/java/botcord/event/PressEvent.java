package botcord.event;

public class PressEvent {

    private Object data;
    private SwitchTarget target;

    public PressEvent(SwitchTarget target) 
    {
        this.target = target;
    }

    public void setData(Object data) 
    {
        this.data = data;
    }

    public Object getData() 
    {
        return data;
    }

    public SwitchTarget getTarget() 
    {
        return target;
    }

    public boolean hasData()
    {
        return this.data != null;
    }

    public static enum Target {

        GUILD,
        PM,
        DEBUG,
        CHANNEL

    }

}
