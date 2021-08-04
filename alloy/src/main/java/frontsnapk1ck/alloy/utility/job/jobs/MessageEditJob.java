package frontsnapk1ck.alloy.utility.job.jobs;

import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.utility.job.JobUtil;
import frontsnapk1ck.utility.event.Job;
import frontsnapk1ck.utility.event.Result;
import net.dv8tion.jda.api.entities.Message;

public class MessageEditJob extends Job {
    
    private Message toEdit;
	private Message editTo;

    public MessageEditJob(Message toEdit, Message editTo)
    {
        this.toEdit = toEdit;
        this.editTo = editTo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result<Void> execute() 
    {
        try 
        {
            this.toEdit.editMessage(editTo).complete();
        }
        catch (Exception e) 
        {
            Alloy.LOGGER.warn("MessageEditJob", e.getMessage());
        }
        return JobUtil.VOID_RESULT;
    }

}
