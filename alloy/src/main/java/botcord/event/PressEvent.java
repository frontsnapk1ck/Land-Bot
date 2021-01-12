package botcord.event;

public class PressEvent {

    private Object data;
    private PressTarget target;

    public PressEvent(PressTarget target) 
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

    public PressTarget getTarget() 
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
