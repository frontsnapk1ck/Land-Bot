package alloy.utility.runnable;

import static alloy.builder.loaders.JobQueueLoaderText.JOB_MAP;
import static alloy.builder.loaders.JobQueueLoaderText.SEPERATION_KEY;

import java.util.concurrent.PriorityBlockingQueue;

import alloy.main.Alloy;
import alloy.main.SendableMessage;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.DisUtil;
import alloy.utility.job.jobs.RemindJob;
import io.Saver;
import utility.StringUtil;
import utility.event.EventManager.ScheduledJob;

public class AlloyShutdownHook extends Thread {

    private static final String SKIP_JOB = "skip job";

    public AlloyShutdownHook(){
    }

    @Override
    public void run() {
        saveQueue();
    }

    private void saveQueue() {
        Saver.clear(AlloyUtil.EVENT_FILE);
        PriorityBlockingQueue<ScheduledJob> queue = Alloy.getQueue();
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

        String out =    "RemindJob" + SEPERATION_KEY + 
                        sJob.time + SEPERATION_KEY;

        RemindJob j = (RemindJob) sJob.job;
        SendableMessage sm = j.getSM();
        String channel = DisUtil.toString(sm.getChannel());
        String mention = sm.getMessage().getContentRaw();
        String[] splitNewLine = sm.getMessage().getEmbeds().get(0).getDescription().split("\n");
        String message = StringUtil.joinStrings(splitNewLine, 1);

        out += channel  +SEPERATION_KEY + mention + SEPERATION_KEY + message;
        return out;
	}

}
