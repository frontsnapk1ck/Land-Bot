package botcord.util;

import java.util.ArrayList;
import java.util.List;

import alloy.main.Alloy;
import alloy.main.intefs.Queueable;
import botcord.collections.UserCollection;
import botcord.util.constants.BCCacheIDs;
import botcord.util.constants.BCColors;
import botcord.util.constants.BCLinks;
import net.dv8tion.jda.api.entities.User;
import utility.cache.Cache;

public class BCUtil implements BCColors, BCLinks, BCCacheIDs {

    private static Cache cache;

    

    public static Cache getCache() 
    {
        return cache;
    }



	public static void loadCache(Queueable alloy) 
    {
        cache = new Cache(alloy);
	}



	public static List<User> loadAllUsers() 
    {
		if (!cache.has(USER_CACHE))
        {        
            UserCollection collection = new UserCollection(new ArrayList<User>());
            cache.put(USER_CACHE , collection);
        }
        UserCollection collection = (UserCollection) cache.get(USER_CACHE);
        return collection.getList();
	}



	public static void loadQueue(Alloy alloy) 
    {
        
	}
    
}
