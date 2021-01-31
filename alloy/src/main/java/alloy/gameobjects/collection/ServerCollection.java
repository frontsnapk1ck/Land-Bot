package alloy.gameobjects.collection;

import java.util.List;

import alloy.gameobjects.Server;

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
