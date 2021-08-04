package frontsnapk1ck.alloy.utility.job.jobs;

import java.util.function.Consumer;

import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.utility.job.JobUtil;
import frontsnapk1ck.utility.event.Job;
import frontsnapk1ck.utility.event.Result;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class DeleteMessageJob extends Job {

    private SendableMessage sm;

    public DeleteMessageJob(SendableMessage sm) 
    {
        this.sm = sm;
	}

    @Override
    @SuppressWarnings("unchecked")
    public Result<Void> execute() 
    {
        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>() 
        {
            @Override
            public void accept(ErrorResponseException t) 
            {
                Alloy.LOGGER.warn("DeleteMessageJob", t.getMessage());
            }

            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE, consumer);

        if (sm.hasSent())
            sm.getSent().delete().queue(null,handler);
        return JobUtil.VOID_RESULT;
        
    }
    
}
