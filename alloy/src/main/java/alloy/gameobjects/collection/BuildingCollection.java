package alloy.gameobjects.collection;

import java.util.List;

import alloy.gameobjects.player.Building;
import utility.cache.AbstractCollection;

public class BuildingCollection extends AbstractCollection<BuildingCollection> {

    public BuildingCollection(List<Building> data) 
    {
        super(data);
    }

    @Override
    public BuildingCollection getData() 
    {
        return this;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Building> getList() 
    {
        return (List<Building>) super.getList();
    }

}
