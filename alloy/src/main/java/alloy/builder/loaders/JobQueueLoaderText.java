package alloy.builder.loaders;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

import alloy.builder.DataLoader;
import alloy.main.Alloy;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.DisUtil;
import alloy.utility.job.jobs.AddUserCoolDownJob;
import alloy.utility.job.jobs.AddUserXPCooldownJob;
import alloy.utility.job.jobs.HelpJob;
import alloy.utility.job.jobs.InviteJob;
import alloy.utility.job.jobs.PingJob;
import alloy.utility.job.jobs.PurgeJob;
import alloy.utility.job.jobs.RemindJob;
import alloy.utility.job.jobs.RmUserCoolDownJob;
import alloy.utility.job.jobs.RmUserXPCooldownJob;
import alloy.utility.job.jobs.SpamRunnable;
import io.FileReader;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import utility.event.EventManager;
import utility.event.EventManager.ScheduledJob;

public class JobQueueLoaderText extends DataLoader<PriorityBlockingQueue<ScheduledJob>, Alloy> {
    
    public static final Map<String , Class<?>> JOB_MAP;
    public static final String  SEPERATION_KEY = "(sep-key)";

    static{
        JOB_MAP = new HashMap<String , Class<?>>();
        configureJobMap();
    }

    public JobQueueLoaderText() 
    {
        
    }

    private static void configureJobMap() 
    {
        JOB_MAP.put(    "AddUserCoolDownJob"      ,        AddUserCoolDownJob.class        );
        JOB_MAP.put(    "AddUserXPCooldownJob"    ,        AddUserXPCooldownJob.class      );
        JOB_MAP.put(    "HelpJob"                 ,        HelpJob.class                   );
        JOB_MAP.put(    "InviteJob"               ,        InviteJob.class                 );
        JOB_MAP.put(    "PingJob"                 ,        PingJob.class                   );
        JOB_MAP.put(    "PurgeJob"                ,        PurgeJob.class                  );
        JOB_MAP.put(    "RemindJob"               ,        RemindJob.class                 );
        JOB_MAP.put(    "RmUserCoolDownJob"       ,        RmUserCoolDownJob.class         );
        JOB_MAP.put(    "RmUserXPCooldownJob"     ,        RmUserXPCooldownJob.class       );
        JOB_MAP.put(    "SpamRunnable"            ,        SpamRunnable.class              );
    }

    @Override
    public PriorityBlockingQueue<ScheduledJob> load(Alloy alloy) 
    {
        PriorityBlockingQueue<ScheduledJob> queue = new PriorityBlockingQueue<ScheduledJob>();
        String[] arr = FileReader.read(AlloyUtil.EVENT_FILE);
        for (String job : arr)
        {
            ScheduledJob sJob = loadSceduledJob(job , alloy);
            if (sJob != null)
                queue.add(sJob);

        }
        return queue;
    }

    private ScheduledJob loadSceduledJob(String job, Alloy alloy) 
    {
        String[] args = job.split(SEPERATION_KEY);
        args = cleanArgs(args);
        String clazzS = args[0];
        if (clazzS.equalsIgnoreCase("RemindJob"))
            return buildRemindJob(args, alloy);
        return null;
    }

    private String[] cleanArgs(String[] args) 
    {
        //  0:"ScheduledJob("
        //  1:")1610218462200("
        //  2:")761746549024751646-761758799093956649("
        //  3:")<@!312743142828933130>("
        //  4:")yes"

        for (int i = 0; i < args.length; i++) 
        {
            if (i == 0)    
                args[i] = args[i].substring(0,args[i].length()-1);
            else if (i == args.length-1)
                args[i] = args[i].substring(1);
            else
                args[i] = args[i].substring(1, args[i].length()-1);
        }
        return args;
    }

    private ScheduledJob buildRemindJob(String[] args, Alloy alloy) 
    {
        Long time = Long.parseLong(args[1]);
        MessageChannel channel = DisUtil.parseChannel(alloy.getJDA() , args[2]);
        String mention = args[3];
        String message = args[4];
    
        Template t = Templates.remindMe( message );
        Message outM = new MessageBuilder()
                            .setEmbed(t.getEmbed())
                            .append(mention)
                            .build();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("RemindCommand {...} RemindJob");
        sm.setMessage(outM);

        RemindJob rJob = new RemindJob(alloy, sm);
        ScheduledJob newSJob = EventManager.newSchedluledJob(time , rJob);
        return newSJob;
        
    }

}
