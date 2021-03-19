package frontsnapk1ck.alloy.command.console;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import frontsnapk1ck.alloy.command.util.AbstractConsoleCommand;
import frontsnapk1ck.alloy.input.console.ConsoleInputData;
import frontsnapk1ck.alloy.utility.job.jobs.DayJob;
import frontsnapk1ck.alloy.utility.job.jobs.PurgeCacheJob;
import frontsnapk1ck.alloy.utility.job.jobs.RmUserCoolDownJob;
import frontsnapk1ck.alloy.utility.job.jobs.RmUserXPCooldownJob;
import frontsnapk1ck.utility.StringUtil;
import frontsnapk1ck.utility.event.EventManager.ScheduledJob;
import frontsnapk1ck.utility.event.Job;
import frontsnapk1ck.utility.event.RepeatingJob;
import frontsnapk1ck.utility.event.Worker;
import frontsnapk1ck.utility.time.TimeUtil;
import frontsnapk1ck.utility.time.TimesIncludes;

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
        else if (args.get(1).equalsIgnoreCase("workers"))
            workers(data);
        else if (args.get(1).equalsIgnoreCase("clear"))
            clearQueue(data);
        else if (args.get(1).equalsIgnoreCase("size"))
            size(data);
    }

    private void workers(ConsoleInputData data) 
    {
        List<Worker> workers = data.getQueue().getWorkers();
        String[][] table = new String[workers.size()][4];
        
        int i = 0;
        for (Worker worker : workers) 
        {
            table[i][0] = worker.getName();
            table[i][1] = Worker.parseState(worker.getState());
            if (worker.isWorking())
                table[i][2] = worker.getJob().getClass().getSimpleName();
            else
                table[i][2] = "N/A";
            table[i][3] = "" + worker.getCompleted();
            i++;
        }
        System.out.println(StringUtil.makeTable(table, new String[]{"~~Worker Name~~","~~State~~","~~Job~~","~~Completed~~"}));
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
