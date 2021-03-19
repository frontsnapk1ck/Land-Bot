package frontsnapk1ck.alloy.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.LRUMap;

import frontsnapk1ck.utility.cache.Cache;
import frontsnapk1ck.utility.cache.Cacheable;

public class AlloyCache extends Cache<String> {

    public Map<String, Cacheable<?>> getCache() 
    {
        Map<String , Cacheable<?>> out = new HashMap<String , Cacheable<?>>();
        LRUMap<String, CacheObject> actual = super.getCacheMap();

        synchronized (actual) 
        {
            MapIterator<String, CacheObject> itr = actual.mapIterator();            
            while (itr.hasNext())
            {
                String key = itr.next();
                CacheObject c = itr.getValue();
                out.put(key, c.value);
            }
        }

        return out;
    }

    public void clear() 
    {
        LRUMap<String, CacheObject> actual = super.getCacheMap();
        List<String> toRm = new ArrayList<String>();

        synchronized (actual) 
        {
            MapIterator<String, CacheObject> itr = actual.mapIterator();            
            while (itr.hasNext())
            {
                String key = itr.next();
                toRm.add(key);
            }
        }
        
        for (String key : toRm) 
        {
            synchronized (actual) 
            {
                actual.remove(key);
            }
        }
    }
    
}
