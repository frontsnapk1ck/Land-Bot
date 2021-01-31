package alloy.gameobjects.collection;

import java.util.List;

import alloy.gameobjects.player.Player;

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
