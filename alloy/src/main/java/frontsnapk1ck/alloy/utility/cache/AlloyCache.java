package frontsnapk1ck.alloy.utility.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapIterator;

import frontsnapk1ck.alloy.audio.GuildMusicManager;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.utility.cache.Cache;
import frontsnapk1ck.utility.cache.CacheObject;
import frontsnapk1ck.utility.cache.Cacheable;
import net.dv8tion.jda.api.entities.Guild;

public class AlloyCache extends Cache<String , Cacheable> {

    public static final long ALLOY_KEEP_TIME = 15000L;
    public static final long GMM_KEEP_TIME = 300000L;

    public AlloyCache() 
    {
        super(ALLOY_KEEP_TIME);
    }


    public Map<String, Cacheable> getCache() 
    {
        Map<String , Cacheable> out = new HashMap<String , Cacheable>();

        synchronized (super.getCacheMap()) 
        {
            MapIterator<String, CacheObject<Cacheable>> itr = super.getCacheMap().mapIterator();            
            while (itr.hasNext())
            {
                String key = itr.next();
                CacheObject<Cacheable> c = itr.getValue();
                out.put(key, c.getValue());
            }
        }

        return out;
    }

    public Map<String, AlloyCacheObject> getCacheObjects() 
    {
        Map<String , AlloyCacheObject> out = new HashMap<String , AlloyCacheObject>();

        synchronized (super.getCacheMap()) 
        {
            MapIterator<String, CacheObject<Cacheable>> itr = super.getCacheMap().mapIterator();            
            while (itr.hasNext())
            {
                String key = itr.next();
                AlloyCacheObject c = (AlloyCacheObject) itr.getValue();
                out.put(key, c);
            }
        }

        return out;
    }

    public void clear() 
    {
        List<String> toRm = new ArrayList<String>();

        synchronized (super.getCacheMap()) 
        {
            MapIterator<String, CacheObject<Cacheable>> itr = super.getCacheMap().mapIterator();            
            while (itr.hasNext())
            {
                String key = itr.next();
                toRm.add(key);
            }
        }
        
        for (String key : toRm) 
        {
            synchronized (super.getCacheMap()) 
            {
                super.getCacheMap().remove(key);
            }
        }
    }

    @Override
    protected void removed(Cacheable value) 
    {
        if (value instanceof GuildMusicManager)
        {
            try 
            {
                GuildMusicManager gmm = (GuildMusicManager) value;
                Guild g = gmm.getGuild();
                g.getAudioManager().closeAudioConnection();
            }
            catch (Exception e)
            {
                Alloy.LOGGER.warn("AlloyCache", e.getMessage());
            }
        }
    }

    @Override
    public void put(String key, Cacheable value, long keepTime) 
    {   
        synchronized (super.getCacheMap()) 
        {
            AlloyCacheObject aco = new AlloyCacheObject(value, keepTime);
            super.getCacheMap().put( key, aco );
        }
    }
    
}
