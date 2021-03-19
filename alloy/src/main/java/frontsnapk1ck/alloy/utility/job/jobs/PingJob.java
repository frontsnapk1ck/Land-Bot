package frontsnapk1ck.alloy.utility.job.jobs;

import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import frontsnapk1ck.utility.event.Job;

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
        sm.setFrom(getClass());
        sm.setMessage("checking ping");
        MessageAction action = bot.getAction( sm );
        if (action != null)
        {
            Message m = action.complete();
            m.editMessage("ping is " + (System.currentTimeMillis() - start) + "ms").queue();
        }
    }


    
}
