package frontsnapk1ck.alloy.utility.job.jobs;

import java.util.List;
import java.util.function.Consumer;

import frontsnapk1ck.alloy.command.administration.PurgeCommand;
import frontsnapk1ck.alloy.main.Alloy;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;
import frontsnapk1ck.utility.event.Job;

public class PurgeJob extends Job {

    private TextChannel channel;
    private List<Message> messagesToDelete;

    public PurgeJob(List<Message> messagesToDelete, TextChannel channel) 
    {
        this.messagesToDelete = messagesToDelete;
        this.channel = channel;
    }

    @Override
    public void execute() 
    {
        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>() 
        {
            @Override
            public void accept(ErrorResponseException t) 
            {
                Alloy.LOGGER.warn("KickCommand", t.getMessage());
            }

            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.INVALID_BULK_DELETE, consumer);

        for (int index = 0; index < messagesToDelete.size(); index += PurgeCommand.MAX_BULK_SIZE) 
        {
            if (messagesToDelete.size() - index < 2)
                messagesToDelete.get(index).delete().queue(null,handler);
            else
                channel.deleteMessages(messagesToDelete.subList(index, Math.min(index + PurgeCommand.MAX_BULK_SIZE, messagesToDelete.size()))).queue(null,handler);
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
