package botcord.collections;

import java.util.List;

import net.dv8tion.jda.api.entities.User;
import utility.cache.AbstractCollection;
import utility.cache.Cacheable;

public class UserCollection extends AbstractCollection<UserCollection> {

    public UserCollection(List<User> data) 
    {
        super(data);
    }

    @Override
    public UserCollection getData() 
    {
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getList() 
    {
        return (List<User>) super.getList();
    }

    @Override
    public long getKeepTime() 
    {
        return Cacheable.FOREVER;
    }
    
}
