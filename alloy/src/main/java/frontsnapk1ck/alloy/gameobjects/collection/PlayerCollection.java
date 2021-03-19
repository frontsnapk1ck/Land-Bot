package frontsnapk1ck.alloy.gameobjects.collection;

import java.util.List;

import frontsnapk1ck.alloy.gameobjects.player.Player;
import frontsnapk1ck.utility.cache.AbstractCollection;

public class PlayerCollection extends AbstractCollection<PlayerCollection> {

    public PlayerCollection(List<Player> players) 
    {
        super(players);
    }

    @Override
    public PlayerCollection getData() 
    {
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Player> getList() 
    {
        return (List<Player>) super.getList();
    }
    
}
