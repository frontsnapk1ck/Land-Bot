package alloy.gameobjects.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alloy.main.Queueable;
import alloy.utility.job.jobs.PurgeCacheJob;

public class Cache {

    public static final long KEEP_TIME = 5000l;

    private Map< String , Cacheable<?> > cache;
    private List<PurgeCacheJob> jobs;
    private Queueable queue;

    private boolean running;

    public Cache(Queueable queue) 
    {
        this.queue = queue;
        
        cache = new HashMap< String , Cacheable<?> >();
        jobs = new ArrayList<PurgeCacheJob>();
    }

    public void put(String key, Cacheable<?> cacheable)
    {
        if (this.cache.containsKey(key))
            return;
        this.cache.put(key, cacheable);
        addToList(key);
    }

    private void addToList(String key) 
    {
        PurgeCacheJob job = new PurgeCacheJob(key);
        this.jobs.add( job );
        this.queue.queueIn(job, KEEP_TIME );
    }

    public Cacheable<?> get(String key)
    {
        updateList(key);
        return this.cache.get(key);
    }

    private void updateList(String key) 
    {
        if (!has(key))
            return;
        PurgeCacheJob job = getJob(key);
        job.disable();
        this.queue.unQueue(job);
        this.jobs.remove(job);
        this.addToList(key);
    }

    public void remove(String key) 
    {
        this.cache.remove(key);
	}

    public PurgeCacheJob getJob(String key) 
    {
        for (PurgeCacheJob j : jobs) 
        {
            if (j.getKey().equalsIgnoreCase(key))
                return j;
        }
        return null;
    }

    public boolean has(String key)
    {
        return this.cache.get(key) != null;
    }

    public boolean isRunning() 
    {
        return running;
    }

    public Map<String, Cacheable<?>> getCache() 
    {
		return this.cache;
	}


    
}
