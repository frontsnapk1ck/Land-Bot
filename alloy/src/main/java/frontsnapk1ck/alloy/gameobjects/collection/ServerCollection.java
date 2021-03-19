package frontsnapk1ck.alloy.gameobjects.collection;

import java.util.List;

import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.utility.cache.AbstractCollection;

public class ServerCollection extends AbstractCollection<ServerCollection> {
    
    public ServerCollection(List<Server> servers) 
    {
        super(servers);
    }

    @Override
    public ServerCollection getData() 
    {
        return this;    
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Server> getList() 
    {
        return (List<Server>) super.getList();
    }
}
