package frontsnapk1ck.alloy.utility.job.jobs;

import frontsnapk1ck.alloy.main.Alloy;
import net.dv8tion.jda.api.entities.Message;
import frontsnapk1ck.utility.event.Job;

public class MessageEditJob extends Job {
    
    private Message toEdit;
	private Message editTo;

    public MessageEditJob(Message toEdit, Message editTo)
    {
        this.toEdit = toEdit;
        this.editTo = editTo;
    }

    @Override
    public void execute() 
    {
        try 
        {
            this.toEdit.editMessage(editTo).complete();
        }
        catch (Exception e) 
        {
            Alloy.LOGGER.warn("MessageEditJob", e.getMessage());
        }
    }

}
