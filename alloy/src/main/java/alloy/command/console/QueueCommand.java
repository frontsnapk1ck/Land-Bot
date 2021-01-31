package alloy.command.console;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import alloy.command.util.AbstractConsoleCommand;
import alloy.input.console.ConsoleInputData;
import alloy.utility.job.jobs.PurgeCacheJob;
import alloy.utility.job.jobs.RmUserCoolDownJob;
import alloy.utility.job.jobs.RmUserXPCooldownJob;
import utility.StringUtil;
import utility.event.EventManager.ScheduledJob;
import utility.event.Job;
import utility.time.TimeUtil;
import utility.time.TimesIncludes;

public class QueueCommand extends AbstractConsoleCommand {

    public static final Class<?>[] saved;

    static 
    {
        saved = configSaved();
    }

    private static Class<?>[] configSaved() 
    {
        return new Class<?>[] {

            RmUserCoolDownJob.class, 
            RmUserXPCooldownJob.class,
            PurgeCacheJob.class,
            
        };
    }

    @Override
    public void execute(ConsoleInputData data) 
    {
        List<String> args = data.getArgs();

        if (args.size() == 1)
            showQueue(data);
        else if (args.get(1).equalsIgnoreCase("clear"))
            clearQueue(data);
    }

    private void clearQueue(ConsoleInputData data) 
    {
        List<String> args = data.getArgs();

        if (args.size() > 2 && args.get(2).equalsIgnoreCase("all")) 
        {
            data.getQueue().getQueue().clear();
            System.err.println("the queue has been cleared");
        }
        else 
        {
            clearPart(data);
            System.err.println("the queue has been partially cleared");
        }
        showQueue(data);
    }

    private void clearPart(ConsoleInputData data) 
    {
        List<ScheduledJob> toRm = new ArrayList<ScheduledJob>();
        PriorityBlockingQueue<ScheduledJob> queue = data.getQueue().getQueue();
        for (ScheduledJob sJob : queue) 
        {
            boolean save = false;
            for (Class<?> c : saved) 
            {
                if (sJob.job.getClass() == c)
                    save = true;
            }
            if (!save)
                toRm.add(sJob);

        }
        data.getQueue().getQueue().removeAll(toRm);
    }

    private void showQueue(ConsoleInputData data) 
    {
        PriorityBlockingQueue<ScheduledJob> queue = data.getQueue().getQueue();
        String[][] tableArr = new String[queue.size()][4];

        int i = 0;
        for (ScheduledJob sjob : queue) {
            Job job = sjob.job;
            String name = job.getClass().isAnonymousClass() ? "Anonymous Class" : job.getClass().getSimpleName();
            long time = sjob.time;

            tableArr[i][0] = name;
            TimesIncludes includes = new TimesIncludes();
            includes.addAll();
            includes.limit(3);
            String longTime = TimeUtil.getTimeTill(time, includes);
            String[] split = longTime.split("\t");
            tableArr[i][1] = split[0];
            tableArr[i][2] = split[1];
            tableArr[i][3] = split[2];

            i++;
        }
        String[] headers = { "~~Class~~", "~~Date~~", "~~Time~~", "~~Time Until~~" };
        String table = StringUtil.makeTable(tableArr, headers);
        System.err.println(table);
    }

}
