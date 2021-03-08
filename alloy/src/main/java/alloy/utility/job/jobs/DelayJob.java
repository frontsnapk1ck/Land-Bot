package alloy.utility.job.jobs;

import java.util.function.Consumer;

import utility.event.Job;

public class DelayJob<T> extends Job {

    private T param;
    private Consumer<? super T> action;

    public DelayJob(Consumer<? super T> action  , T param) 
    {
        this.action = action;
        this.param = param;
    }

	@Override
    public void execute() 
    {
        action.accept(param);
    }
    
}
