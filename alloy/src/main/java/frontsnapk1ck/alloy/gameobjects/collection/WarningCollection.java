package frontsnapk1ck.alloy.gameobjects.collection;

import java.util.List;

import frontsnapk1ck.alloy.gameobjects.Warning;
import frontsnapk1ck.utility.cache.AbstractCollection;

public class WarningCollection extends AbstractCollection<WarningCollection> {
    
    public WarningCollection(List<Warning> warnings) 
    {
        super(warnings);
    }

    @Override
    public WarningCollection getData() 
    {
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Warning> getList() 
    {
        return (List<Warning>) super.getList();
    }
}
