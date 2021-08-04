package frontsnapk1ck.alloy.utility.job.jobs;

import frontsnapk1ck.alloy.main.intefs.handler.CooldownHandler;
import frontsnapk1ck.alloy.utility.job.JobUtil;
import frontsnapk1ck.utility.event.Job;
import frontsnapk1ck.utility.event.Result;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class AddUserCoolDownJob extends Job {

    private Guild guild;
    private User author;
    private CooldownHandler handler;

    public AddUserCoolDownJob(Guild guild, User author, CooldownHandler handler) 
    {
        this.guild = guild;
        this.author = author;
        this.handler = handler;
	}

	@Override
    @SuppressWarnings("unchecked")
    public Result<Void> execute() 
    {
        Member m = guild.getMember(author);
        handler.addCooldownUser( m );
        return JobUtil.VOID_RESULT;
    }
    
}
