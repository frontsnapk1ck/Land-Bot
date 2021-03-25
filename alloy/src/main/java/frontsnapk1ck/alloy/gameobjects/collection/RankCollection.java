package frontsnapk1ck.alloy.gameobjects.collection;

import java.util.List;

import frontsnapk1ck.alloy.gameobjects.player.Rank;
import frontsnapk1ck.utility.cache.AbstractCollection;

public class RankCollection extends AbstractCollection<Rank> {
    
    public RankCollection(List<Rank> ranks) 
    {
        super(ranks);
    }

    @Override
    public RankCollection getData() 
    {
        return this;
    }

}
