package frontsnapk1ck.alloy.utility.job.jobs;

import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import frontsnapk1ck.utility.event.Job;

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
    public void execute() 
    {
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(this.message);
        this.sendable.send(sm);
    }
    
}
