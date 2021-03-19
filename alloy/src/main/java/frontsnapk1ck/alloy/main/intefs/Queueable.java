package frontsnapk1ck.alloy.main.intefs;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import frontsnapk1ck.utility.event.EventManager.ScheduledJob;
import frontsnapk1ck.utility.event.Job;
import frontsnapk1ck.utility.event.Worker;

public interface Queueable {

    
    public void queueIn( Job action , long offset );

    public void queue( Job action );

    public PriorityBlockingQueue<ScheduledJob> getQueue();

	public boolean unQueue( Job job);

	public List<Worker> getWorkers();

}
