package frontsnapk1ck.alloy.input.console;

import frontsnapk1ck.input.Input;

public class ConsoleInput extends Input {

    private ConsoleInputData data;
    private String description;

    public ConsoleInput(String name , String trigger , ConsoleInputData data) 
    {
        super(name, trigger);
        this.data = data;
    }

    public ConsoleInput(String name, String trigger, String description) 
    {
        super(name, trigger);
        this.description = description;
	}

    public ConsoleInputData getData() 
    {
		return this.data;
    }
    public String getDescription() {
        return description;
    }
    
}
