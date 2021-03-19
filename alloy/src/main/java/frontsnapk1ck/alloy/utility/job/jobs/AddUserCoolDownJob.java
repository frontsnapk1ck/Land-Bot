package frontsnapk1ck.alloy.utility.job.jobs;

import frontsnapk1ck.alloy.main.intefs.handler.CooldownHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import frontsnapk1ck.utility.event.Job;

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
    public void execute() 
    {
        Member m = guild.getMember(author);
        handler.addCooldownUser( m );
        
    }
    
}
