package alloy.gameobjects.collection;

import java.util.List;

import alloy.gameobjects.player.Rank;
import utility.cache.AbstractCollection;

public class RankCollection extends AbstractCollection<RankCollection> {
    
    public RankCollection(List<Rank> ranks) 
    {
        super(ranks);
    }

    @Override
    public RankCollection getData() 
    {
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Rank> getList() 
    {
        return (List<Rank>) super.getList();
    }
}
