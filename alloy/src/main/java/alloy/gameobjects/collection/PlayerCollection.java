package alloy.gameobjects.collection;

import java.util.List;

import alloy.gameobjects.player.Player;
import utility.cache.AbstractCollection;

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
    
    @Override
    public long getKeepTime() 
    {
        return 5000;
    }
}
