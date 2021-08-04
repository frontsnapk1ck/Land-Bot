package frontsnapk1ck.alloy.utility.job.jobs;

import frontsnapk1ck.alloy.main.intefs.handler.CooldownHandler;
import frontsnapk1ck.alloy.utility.job.JobUtil;
import frontsnapk1ck.utility.event.Job;
import frontsnapk1ck.utility.event.Result;
import net.dv8tion.jda.api.entities.Member;

public class AddUserXPCooldownJob extends Job {

    private Member m;
    private CooldownHandler handler;

    public AddUserXPCooldownJob(CooldownHandler handler, Member m) 
    {
        this.m = m;
        this.handler = handler;
	}

	@Override
    @SuppressWarnings("unchecked")
    public Result<Void> execute() 
    {
        handler.addXpCooldownUser(m);
        return JobUtil.VOID_RESULT;
    }

    
}
