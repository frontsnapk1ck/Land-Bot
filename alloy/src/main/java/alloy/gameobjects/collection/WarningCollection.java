package alloy.gameobjects.collection;

import java.util.List;

import alloy.gameobjects.Warning;
import utility.cache.AbstractCollection;

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
