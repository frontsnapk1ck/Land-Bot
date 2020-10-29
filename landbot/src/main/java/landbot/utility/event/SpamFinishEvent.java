package landbot.utility.event;

import landbot.utility.timer.SpamRunnable;

public class SpamFinishEvent {

    private SpamRunnable runnable;


    public SpamRunnable getRunnable() {
        return runnable;
    }

    public void setRunnable(SpamRunnable runnable) {
        this.runnable = runnable;
    }
    
}
