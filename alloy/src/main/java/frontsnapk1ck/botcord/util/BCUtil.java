package frontsnapk1ck.botcord.util;

import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.utility.AlloyCache;
import frontsnapk1ck.botcord.collections.UserCollection;
import frontsnapk1ck.botcord.util.constants.BCCacheIDs;
import frontsnapk1ck.botcord.util.constants.BCColors;
import frontsnapk1ck.botcord.util.constants.BCLinks;
import net.dv8tion.jda.api.entities.User;

public class BCUtil implements BCColors, BCLinks, BCCacheIDs {

    private static AlloyCache cache;

    

    public static AlloyCache getCache() 
    {
        return cache;
    }



	public static void loadCache() 
    {
        cache = new AlloyCache();
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
