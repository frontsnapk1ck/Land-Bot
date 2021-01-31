package alloy.utility.job;

import alloy.utility.job.jobs.SpamRunnable;
import utility.event.EventManager;
import utility.event.Job;

public class AlloyEventHandler extends EventManager {

    public void queue(SpamRunnable action) 
    {
        Thread spamThread = new Thread( new Runnable()
        {
            @Override
            public void run() 
            {
                action.execute();
            };
        } );
        spamThread.setName("SpamThread - " + action.getID() );
        spamThread.setDaemon(true);
        spamThread.start();
    }
    
}
