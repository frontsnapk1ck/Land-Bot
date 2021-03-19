package frontsnapk1ck.botcord.collections;

import java.util.List;

import frontsnapk1ck.utility.cache.AbstractCollection;
import net.dv8tion.jda.api.entities.User;

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
    
}
