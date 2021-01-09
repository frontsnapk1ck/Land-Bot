package input;

public class Input {

    private String name;
    private String trigger;

    public Input(String name , String trigger) 
    {
        this.name = name;
        this.trigger = trigger;
    }

    public String getName() 
    {
		return name;
    }

    public String getTrigger() {
        return trigger;
    }
        
    @Override
    public boolean equals(Object obj) 
    {        
        if (obj instanceof Input)
        {
            Input i = (Input) obj;
            return  i.getTrigger().equalsIgnoreCase(this.getTrigger()) &&
                    i.getName().equalsIgnoreCase(this.getName());
        }
        return false;
    }

    protected void setTrigger(String trigger) 
    {
        this.trigger = trigger;
	}


}
