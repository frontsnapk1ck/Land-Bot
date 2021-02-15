package alloy.utility.job.jobs;

import alloy.main.intefs.handler.CooldownHandler;
import net.dv8tion.jda.api.entities.Member;
import utility.event.Job;

public class RmUserXPCooldownJob extends Job {

    private Member m;
    private CooldownHandler handler;

    public RmUserXPCooldownJob(CooldownHandler handler, Member m) 
    {
        this.m = m;
        this.handler = handler;
	}

	@Override
    public void execute() 
    {
        handler.removeXpCooldownUser(m);
    }

    
}
