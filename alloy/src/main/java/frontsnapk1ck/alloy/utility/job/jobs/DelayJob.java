package frontsnapk1ck.alloy.utility.job.jobs;

import java.util.function.Consumer;

import frontsnapk1ck.alloy.utility.job.JobConsumer;
import frontsnapk1ck.alloy.utility.job.JobUtil;
import frontsnapk1ck.utility.event.Job;
import frontsnapk1ck.utility.event.Result;

public class DelayJob< R > extends Job {

    private JobConsumer<R> action;

    public DelayJob(JobConsumer<R> action)
    {
        this.action = action;
    }


    @SuppressWarnings("unchecked")
    public <T> DelayJob( Consumer<? super T> action  , T param , R anyObj) 
    {
        if (anyObj != null)
            throw new RuntimeException("Return type is not Void");
        
        this.action = (JobConsumer<R>) JobUtil.parseJobConsumer(action, param);
    }

	@Override
    @SuppressWarnings("unchecked")
    public Result<R> execute() 
    {
        return action.execute();
    }
    
}
