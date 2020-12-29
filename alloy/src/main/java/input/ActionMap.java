package input;

import java.util.ArrayList;
import java.util.List;

public class ActionMap {

    private List<String> ids;
    private List<InputAction> actions;
    
    public ActionMap() 
    {
        this.ids = new ArrayList<String>();
        this.actions = new ArrayList<InputAction>();

    }

    public void put(String key, InputAction value) 
    {
        // remove duplicates
        int i = this.ids.indexOf( key );
        if ( i >= 0 )
        {
            this.ids.remove( i );
            this.actions.remove( i );
        }
        
        // Add mapping
        this.ids.add(key);
        this.actions.add(value);
	}

    public InputAction get(String id) 
    {
        int i = this.ids.indexOf(id);
        if (i >=0)
            return this.actions.get(i);
        return null;
    }

    public boolean containsID(String key)
    {
        for (String id : ids) 
        {
            if (id.equalsIgnoreCase(key))
                return true;  
        }
        return false;
    }

    public List<String> getIds() {
        return ids;
    }

    
    
}