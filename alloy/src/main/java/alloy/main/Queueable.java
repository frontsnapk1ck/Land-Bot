package alloy.main;

import java.util.concurrent.PriorityBlockingQueue;

import utility.event.EventManager.ScheduledJob;
import utility.event.Job;

public interface Queueable {

    
    public void queueIn( Job action , long offset );

    public void queue( Job action );

    public PriorityBlockingQueue<ScheduledJob> getQueue();

	public boolean unQueue( Job job);

}
