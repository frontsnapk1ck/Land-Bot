package frontsnapk1ck.alloy.utility.job.jobs;

import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.utility.job.JobUtil;
import frontsnapk1ck.utility.event.Job;
import frontsnapk1ck.utility.event.Result;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SendMessageJob extends Job {

    private Message message;
    private TextChannel channel;
    private Sendable sendable;

    public SendMessageJob(Sendable sendable, TextChannel channel, Message message) 
    {
        this.sendable = sendable;
        this.channel = channel;
        this.message = message;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result<Void> execute() 
    {
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(this.message);
        this.sendable.send(sm);
        return JobUtil.VOID_RESULT;
    }
    
}
