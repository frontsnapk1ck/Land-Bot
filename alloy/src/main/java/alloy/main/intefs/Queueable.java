package alloy.main.intefs;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import utility.event.EventManager.ScheduledJob;
import utility.event.Job;
import utility.event.Worker;

public interface Queueable {

    
    public void queueIn( Job action , long offset );

    public void queue( Job action );

    public PriorityBlockingQueue<ScheduledJob> getQueue();

	public boolean unQueue( Job job);

	public List<Worker> getWorkers();

}
