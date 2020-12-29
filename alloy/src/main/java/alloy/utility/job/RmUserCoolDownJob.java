package alloy.utility.job;

import alloy.main.handler.CooldownHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import utility.event.Job;

public class RmUserCoolDownJob extends Job {

    private Guild guild;
    private User author;
    private CooldownHandler handler;

    public RmUserCoolDownJob(Guild guild, User author, CooldownHandler handler) 
    {
        this.guild = guild;
        this.author = author;
        this.handler = handler;
	}

    @Override
    protected void execute() 
    {
        Member m = guild.getMember(author);
        handler.removeCooldownUser( m );
    }

    
}
