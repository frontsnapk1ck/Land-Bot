package alloy.utility.event;

import alloy.utility.job.jobs.SpamRunnable;

public class SpamFinishEvent {

    private SpamRunnable runnable;


    public SpamRunnable getRunnable() {
        return runnable;
    }

    public void setRunnable(SpamRunnable runnable) {
        this.runnable = runnable;
    }
    
}
