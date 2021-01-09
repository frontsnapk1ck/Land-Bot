package alloy.command.console;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import alloy.command.util.AbstractConsoleCommand;
import alloy.main.Alloy;
import net.dv8tion.jda.api.JDA;
import utility.event.EventManager.ScheduledJob;
import utility.time.TimeUtil;
import utility.time.TimesIncludes;
import utility.StringUtil;
import utility.event.Job;

public class QueueCommand extends AbstractConsoleCommand {

    @Override
    public void execute(List<String> args, JDA jda) 
    {
        PriorityBlockingQueue<ScheduledJob> queue = Alloy.getQueue();
        String[][] tableArr = new String[queue.size()][4];
        
        int i = 0;
        for (ScheduledJob sjob : queue) 
        {
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
        String[] headers = { "~~Class~~" , "~~Date~~", "~~Time~~" , "~~Time Untill~~" };
        String table = StringUtil.makeTable(tableArr, headers);
        System.err.println(table);
    }
    
}
