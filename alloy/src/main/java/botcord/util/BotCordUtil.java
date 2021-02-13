package botcord.util;

import java.util.ArrayList;
import java.util.List;

import alloy.main.Queueable;
import botcord.collections.UserCollection;
import botcord.util.constants.BotCordCacheIDs;
import botcord.util.constants.BotCordColors;
import botcord.util.constants.BotCordLinks;
import net.dv8tion.jda.api.entities.User;
import utility.cache.Cache;

public class BotCordUtil implements BotCordColors, BotCordLinks, BotCordCacheIDs {

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
    
}
