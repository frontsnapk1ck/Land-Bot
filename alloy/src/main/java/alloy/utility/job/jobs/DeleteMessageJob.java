package alloy.utility.job.jobs;

import alloy.main.SendableMessage;
import utility.event.Job;

public class DeleteMessageJob extends Job {

    private SendableMessage sm;

    public DeleteMessageJob(SendableMessage sm) 
    {
        this.sm = sm;
	}

	@Override
    protected void execute() 
    {
        if (sm.hasSent())
            sm.getSent().delete().queue();
    }
    
}
