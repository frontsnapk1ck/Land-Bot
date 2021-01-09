package alloy.command.console;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import alloy.command.util.AbstractConsoleCommand;
import alloy.main.Alloy;
import net.dv8tion.jda.api.JDA;
import utility.event.EventManager.ScheduledJob;
import utility.time.TimeUtil;
import utility.StringUtil;
import utility.event.Job;

public class QueueCommand extends AbstractConsoleCommand {

    @Override
    public void execute(List<String> args, JDA jda) 
    {
        PriorityBlockingQueue<ScheduledJob> queue = Alloy.getQueue();
        String[][] tableArr = new String[queue.size()][2];
        
        int i = 0;
        for (ScheduledJob sjob : queue) 
        {
            Job job = sjob.job;
            String name = job.getClass().isAnonymousClass() ? "Anonymous Class" : job.getClass().getSimpleName();
            long time = sjob.time;

            tableArr[i][0] = name;
            tableArr[i][1] = TimeUtil.getMidTime(time);
            
            i++;
        }
        String[] headers = { "~~Class~~" , "~~Time (local)~~" };
        String table = StringUtil.makeTable(tableArr, headers);
        System.err.println(table);
    }
    
}
