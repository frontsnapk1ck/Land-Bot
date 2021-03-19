package frontsnapk1ck.alloy.utility.job.jobs;

import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.utility.event.Job;

public class RemindJob extends Job {

    private Sendable bot;
    private SendableMessage sm;

    public RemindJob(Sendable bot, SendableMessage sm) 
    {
        super();
        this.bot = bot;
        this.sm = sm;
    }

    @Override
    public void execute() 
    {
        this.bot.send(sm);
    }

    public SendableMessage getSM() 
    {
		return this.sm;
	}
    
}
