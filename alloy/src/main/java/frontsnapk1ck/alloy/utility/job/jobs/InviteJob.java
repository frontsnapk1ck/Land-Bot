package frontsnapk1ck.alloy.utility.job.jobs;

import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.job.JobUtil;
import frontsnapk1ck.utility.event.Job;
import frontsnapk1ck.utility.event.Result;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class InviteJob extends Job {

    private Sendable bot;
    private Member m;

    public InviteJob(Member m, Sendable bot) 
    {
        this.m = m;
        this.bot = bot;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result<Void> execute() 
    {
        PrivateChannel pc = m.getUser().openPrivateChannel().complete();
        AlloyTemplate t = Templates.inviteActual(m);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(pc);
        sm.setMessage(t.getEmbed());
        sm.setFrom(getClass());
        bot.send(sm);
        return JobUtil.VOID_RESULT;
    }
    
}
