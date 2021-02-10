package alloy.utility.job.jobs;

import alloy.main.Sendable;
import alloy.main.SendableMessage;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import utility.event.Job;

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
        sm.setFrom("SendMessageJob");
        sm.setMessage(this.message);
        this.sendable.send(sm);
    }
    
}
