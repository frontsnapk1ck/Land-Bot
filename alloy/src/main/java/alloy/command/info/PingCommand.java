package alloy.command.info;

import alloy.command.util.AbstractCommand;
import alloy.input.discord.AlloyInputData;
import alloy.main.Queueable;
import alloy.main.Sendable;
import alloy.utility.job.jobs.PingJob;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.event.Job;

public class PingCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        User author = data.getUser();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Message msg = data.getMessageActual();
        Queueable q = data.getQueue();
     
        String name = "Ping Check in " + msg.getGuild().getName() + " requested by " + author.getName() + "\t\t" + author.getId();
        Job j = new PingJob(channel , bot , name);
        q.queue(j);

    }

}
