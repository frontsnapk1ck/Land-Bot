package alloy.utility.job.jobs;

import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import utility.event.Job;

public class PingJob extends Job {

    private TextChannel channel;
    private Sendable bot;

    public PingJob(TextChannel channel, Sendable bot, String name) 
    {
        super(name);
        this.channel = channel;
        this.bot = bot;
    }

    @Override
    public void execute() 
    {
        long start = System.currentTimeMillis();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("PingCommand");
        sm.setMessage("checking ping");
        MessageAction action = bot.getAction( sm );
        if (action != null)
        {
            Message m = action.complete();
            m.editMessage("ping is " + (System.currentTimeMillis() - start) + "ms").queue();
        }
    }


    
}
