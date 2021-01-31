package alloy.utility.job.jobs;

import alloy.utility.discord.AlloyUtil;
import utility.event.Job;

public class PurgeCacheJob extends Job {

    private String key;

    public PurgeCacheJob(String key) 
    {
        this.key = key;
    }

    @Override
	public void execute() 
    {
        AlloyUtil.getCache().remove(key);
    }

    public String getKey() 
    {
        return key;
    }
    
}
