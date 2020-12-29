package alloy.utility.job;

import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
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
    protected void execute() 
    {
        PrivateChannel pc = m.getUser().openPrivateChannel().complete();
        Template t = Templates.inviteActual(m);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(pc);
        sm.setMessage(t.getEmbed());
        sm.setFrom("InviteCommand");
        bot.send(sm);
    }
    
}
