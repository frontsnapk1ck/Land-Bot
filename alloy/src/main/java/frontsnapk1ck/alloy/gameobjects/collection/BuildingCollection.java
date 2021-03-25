package frontsnapk1ck.alloy.gameobjects.collection;

import java.util.List;

import frontsnapk1ck.alloy.gameobjects.player.Building;
import frontsnapk1ck.utility.cache.AbstractCollection;

public class BuildingCollection extends AbstractCollection<Building> {

    public BuildingCollection(List<Building> data) 
    {
        super(data);
    }

    @Override
    public BuildingCollection getData() 
    {
        return this;
    }

}
