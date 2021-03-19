package frontsnapk1ck.input;

import java.util.ArrayList;
import java.util.List;

public class InputMap {

    private List<String> ids;
    private List<Input>  inputs;

    public InputMap( ) 
    {
        this.inputs = new ArrayList<Input>();
        this.ids = new ArrayList<String>();
    }

    public void put(Input key, String value) 
    {
        // remove duplicates
        int i = this.inputs.indexOf( key );
        if ( i >= 0 )
        {
            this.inputs.remove( i );
            this.ids.remove( i );
        }
        
        // Add mapping
        this.inputs.add(key);
        this.ids.add(value);
	}

    public boolean containsID(String command)
    {
        for (Input input : inputs) 
        {
            if (input.getTrigger().equalsIgnoreCase(command))
                return true;  
        }
        return false;
    }

    public boolean contains(Input i) 
    {
        for (Input input : inputs) 
        {
            if (i.equals(input))    
                return true;
        }
        return false;
    }

    public String get(Input input) 
    {
        int i = this.inputs.indexOf(input);
        if (i >=0)
            return this.ids.get(i);
        return null;
	}

    public List<Input> getInputs() {
        return inputs;
    }
    
}