package alloy.command.console;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import alloy.command.util.AbstractConsoleCommand;
import alloy.input.console.ConsoleInputData;
import alloy.utility.job.jobs.DayJob;
import alloy.utility.job.jobs.PurgeCacheJob;
import alloy.utility.job.jobs.RmUserCoolDownJob;
import alloy.utility.job.jobs.RmUserXPCooldownJob;
import utility.StringUtil;
import utility.event.EventManager.ScheduledJob;
import utility.event.Job;
import utility.event.RepeatingJob;
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
            DayJob.class,
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
        else if (args.get(1).equalsIgnoreCase("size"))
            size(data);
    }

    private void size(ConsoleInputData data) 
    {
        int size = data.getQueue().getQueue().size();
        System.out.println("the size of the queue is: " + size);
    }

    private void clearQueue(ConsoleInputData data) 
    {
        List<String> args = data.getArgs();

        if (args.size() > 2 && args.get(2).equalsIgnoreCase("all")) 
        {
            data.getQueue().getQueue().clear();
            System.out.println("the queue has been cleared");
        }
        else 
        {
            clearPart(data);
            System.out.println("the queue has been partially cleared");
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
        String[][] tableArr = new String[queue.size()][5];

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
            if (job instanceof RepeatingJob)
            {
                RepeatingJob rj = (RepeatingJob)job;
                String timeTill = TimeUtil.getRelativeTime(rj.getRepTime() , true , true);
                tableArr[i][4] = "Y\t(" + timeTill + ")";
            }
            else
                tableArr[i][4] = "N";

            i++;
        }
        String[] headers = { "~~Class~~", "~~Date~~", "~~Time~~", "~~Time Until~~" , "~~Repeating~~"};
        String table = StringUtil.makeTable(tableArr, headers);
        System.out.println(table);
    }

}
