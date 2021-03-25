package frontsnapk1ck.alloy.gameobjects.collection;

import java.util.List;

import frontsnapk1ck.alloy.gameobjects.player.Player;
import frontsnapk1ck.utility.cache.AbstractCollection;

public class PlayerCollection extends AbstractCollection<Player> {

    public PlayerCollection(List<Player> players) 
    {
        super(players);
    }

    @Override
    public PlayerCollection getData() 
    {
        return this;
    }
    
}
