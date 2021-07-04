package frontsnapk1ck.alloy.utility.runnable;

import static frontsnapk1ck.alloy.io.loader.JobQueueLoaderText.JOB_MAP;
import static frontsnapk1ck.alloy.io.loader.JobQueueLoaderText.SEPARATION_KEY;

import java.util.concurrent.PriorityBlockingQueue;

import frontsnapk1ck.alloy.io.saver.EventQueueSaver;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.main.intefs.Queueable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.discord.DisUtil;
import frontsnapk1ck.alloy.utility.job.jobs.RemindJob;
import frontsnapk1ck.io.Saver;
import frontsnapk1ck.utility.StringUtil;
import frontsnapk1ck.utility.event.EventManager.ScheduledJob;

public class AlloyShutdownHook extends Thread {

    private static final String SKIP_JOB = "skip job";
    private Queueable queue;

    public AlloyShutdownHook(Queueable queue)
    {
        this.queue = queue;
    }

    @Override
    public void run() 
    {
        saveQueue();
        EventQueueSaver eqs = new EventQueueSaver();
        eqs.save(queue.getQueue(), AlloyUtil.ALLOY_PATH + "res/queue.xml" );
    }

    private void saveQueue() 
    {
        Saver.clear(AlloyUtil.EVENT_FILE);
        PriorityBlockingQueue<ScheduledJob> queue = this.queue.getQueue();
        for (ScheduledJob scheduledJob : queue) 
        {
            String save = getSave(scheduledJob);
            if (!save.equalsIgnoreCase(SKIP_JOB))
                Saver.saveAppend(AlloyUtil.EVENT_FILE, save);
        }
        Alloy.LOGGER.info("AlloyShutdownHook", "the event queue has been saved");

    }

    private String getSave(ScheduledJob sJob) {
        String clazzS = sJob.job.getClass().getSimpleName();
        if (!JOB_MAP.containsKey(clazzS))
            return SKIP_JOB;

        Class<?> clazz = JOB_MAP.get(clazzS);

        if (clazz == RemindJob.class)
            return loadRemindJobClass(sJob);

        return SKIP_JOB;

    }

    private String loadRemindJobClass(ScheduledJob sJob) 
    {

        String out =    "RemindJob" + SEPARATION_KEY + 
                        sJob.time + SEPARATION_KEY;

        RemindJob j = (RemindJob) sJob.job;
        SendableMessage sm = j.getSM();
        String channel = DisUtil.toString(sm.getChannel());
        String mention = sm.getMessage().getContentRaw();
        String[] splitNewLine = sm.getMessage().getEmbeds().get(0).getDescription().split("\n");
        String message = StringUtil.joinStrings(splitNewLine, 1);

        out +=  channel  +SEPARATION_KEY + 
                mention + SEPARATION_KEY + 
                message;
        
        return out;
	}

}
