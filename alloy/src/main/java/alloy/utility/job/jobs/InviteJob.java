package alloy.utility.job.jobs;

import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import disterface.util.template.Template;
import alloy.templates.Templates;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import utility.event.Job;

public class InviteJob extends Job {

    private Sendable bot;
    private Member m;

    public InviteJob(Member m, Sendable bot) 
    {
        this.m = m;
        this.bot = bot;
    }

    @Override
    public void execute() 
    {
        PrivateChannel pc = m.getUser().openPrivateChannel().complete();
        Template t = Templates.inviteActual(m);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(pc);
        sm.setMessage(t.getEmbed());
        sm.setFrom(getClass());
        bot.send(sm);
    }
    
}
