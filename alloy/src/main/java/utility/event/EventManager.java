package utility.event;

import java.util.concurrent.PriorityBlockingQueue;

import alloy.main.Alloy;
import alloy.utility.job.jobs.RemindJob;

public class EventManager {

    public static final Long COOLDOWN_INTERVAL = 50l;

    private static final EventManager fake = new EventManager(false);

    private PriorityBlockingQueue<ScheduledJob> jobQueue;

    private boolean running;
    private Thread eventThread;

    public EventManager() {
        this.jobQueue = new PriorityBlockingQueue<ScheduledJob>();
        init();
    }

    private EventManager(boolean b) {
    }

    private void init() {
        this.eventThread = new Thread(new Runnable() {
            @Override
            public void run() {
                eventLoop();
            }
        });

        this.eventThread.setName("EventManager Thread");
        this.eventThread.setDaemon(true);
        this.eventThread.start();
    }

    public void queueIn(Job action, long offset) {
        long time = System.currentTimeMillis();
        time += offset;
        this.queue(time, action);
    }

    public void queue(Job action) {
        long time = System.currentTimeMillis();
        this.queue(time, action);
    }

    public void queue(long time, Job job) {
        ScheduledJob scheduled = new ScheduledJob(time, job);
        this.jobQueue.add(scheduled);
    }

    public boolean unQueue(Job job) 
    {
        ScheduledJob toRm = null;
        for (ScheduledJob sJob : jobQueue) 
        {
            if (sJob.job == job)
                toRm = sJob;
        }
        if (toRm != null)
            return this.jobQueue.remove(toRm);
        return false;
	}

    protected void eventLoop() 
    {
        this.running = true;
        while (this.running) {
            // Look at next job
            ScheduledJob next = this.jobQueue.peek();

            // Check time
            Long time = System.currentTimeMillis();

            if ( next != null && next.time <= time )
                this.executeNextJob();

            // Otherwise, pause if no new jobs...
            // - if you still have jobs and its time
            // for them to execute we dont' want to
            // pause, we want to run all of them.
            else
                sleep();
        }
    }

    protected void sleep() {
        try {
            Thread.sleep(COOLDOWN_INTERVAL);
        } catch (InterruptedException e) {
        }
    }

    private void executeNextJob() 
    {
        try {
            // Remove the next job from queue
            // - same one you just "peeked" at.
            ScheduledJob job = this.jobQueue.take();

            // execute the job
            if (job.job.toExecute())
                job.job.execute();
        } catch (InterruptedException ex){
            Alloy.LOGGER.debug("EventManager", ex );
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void stop()
    {
        this.running = false;
    }

    public PriorityBlockingQueue<ScheduledJob> getJobQueue() {
        return jobQueue;
    }

    public void setJobQueue(PriorityBlockingQueue<ScheduledJob> jobQueue) {
        this.jobQueue = jobQueue;
    }
    


    public static ScheduledJob newScheduledJob(Long time, RemindJob job) 
    {
		return fake.newS(time,job);
    }
    
    private ScheduledJob newS(Long time, RemindJob job) 
    {
        return new ScheduledJob(time, job);
    }

    // ====================================================================
    
    public class ScheduledJob implements Comparable<ScheduledJob> {
		
		public Long time;
		public Job job;
		
		public ScheduledJob( Long t, Job j )
		{
			this.time = t;
			this.job = j;
		}
		
		@Override public int compareTo( ScheduledJob other )
		{
			// - Return a negative value if THIS job 
			//		should come first
			//		ie; 	other.time > this.time
			// - Return a positive value if THIS job
			//		should come after
			// - Return zero if same time;
			
			return		(int) (this.time - other.time);
		}
	}
}
