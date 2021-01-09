package alloy.utility.job.jobs;

import java.util.List;

import alloy.command.administration.PurgeCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import utility.event.Job;

public class PurgeJob extends Job {

    private TextChannel channel;
    private List<Message> messagesToDelete;

    public PurgeJob(List<Message> messagesToDelete, TextChannel channel) 
    {
        this.messagesToDelete = messagesToDelete;
        this.channel = channel;
    }

    @Override
    protected void execute() 
    {
        for (int index = 0; index < messagesToDelete.size(); index += PurgeCommand.MAX_BULK_SIZE) 
        {
            if (messagesToDelete.size() - index < 2)
                messagesToDelete.get(index).delete().queue();
            else
                channel.deleteMessages(messagesToDelete.subList(index, Math.min(index + PurgeCommand.MAX_BULK_SIZE, messagesToDelete.size()))).queue();
            cooldown(2000l);
        }
    }

    private void cooldown(long l) 
    {
        try 
        {
            Thread.sleep(l);
        } 
        catch (Exception ignored){
        }
    }

}
