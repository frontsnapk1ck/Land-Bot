package alloy.utility.job.jobs;

import alloy.main.Alloy;
import net.dv8tion.jda.api.entities.Message;
import utility.event.Job;

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
