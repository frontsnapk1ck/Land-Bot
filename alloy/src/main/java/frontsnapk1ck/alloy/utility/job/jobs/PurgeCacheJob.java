package frontsnapk1ck.alloy.utility.job.jobs;

import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.utility.event.Job;

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
