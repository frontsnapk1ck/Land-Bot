package frontsnapk1ck.alloy.gameobjects.collection;

import java.util.List;

import frontsnapk1ck.alloy.gameobjects.player.Rank;
import frontsnapk1ck.utility.cache.AbstractCollection;

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
