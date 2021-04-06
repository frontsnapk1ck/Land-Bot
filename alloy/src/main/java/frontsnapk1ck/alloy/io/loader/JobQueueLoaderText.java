package frontsnapk1ck.alloy.io.loader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

import frontsnapk1ck.alloy.io.loader.util.JobQueueData;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.DisUtil;
import frontsnapk1ck.alloy.utility.job.jobs.AddUserCoolDownJob;
import frontsnapk1ck.alloy.utility.job.jobs.AddUserXPCooldownJob;
import frontsnapk1ck.alloy.utility.job.jobs.InviteJob;
import frontsnapk1ck.alloy.utility.job.jobs.RemindJob;
import frontsnapk1ck.alloy.utility.job.jobs.RmUserCoolDownJob;
import frontsnapk1ck.alloy.utility.job.jobs.RmUserXPCooldownJob;
import frontsnapk1ck.alloy.utility.job.jobs.SendMessageJob;
import frontsnapk1ck.disterface.util.template.Template;
import frontsnapk1ck.io.DataLoader;
import frontsnapk1ck.io.FileReader;
import frontsnapk1ck.utility.event.EventManager;
import frontsnapk1ck.utility.event.EventManager.ScheduledJob;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class JobQueueLoaderText extends DataLoader<PriorityBlockingQueue<ScheduledJob>, JobQueueData> {
    
    public static final Map<String , Class<?>> JOB_MAP;
    public static final String  SEPARATION_KEY = "(sep-key)";

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
        JOB_MAP.put(    "InviteJob"               ,        InviteJob.class                 );
        JOB_MAP.put(    "RemindJob"               ,        RemindJob.class                 );
        JOB_MAP.put(    "RmUserCoolDownJob"       ,        RmUserCoolDownJob.class         );
        JOB_MAP.put(    "RmUserXPCooldownJob"     ,        RmUserXPCooldownJob.class       );
        JOB_MAP.put(    "SendMessageJob"          ,        SendMessageJob.class            );
    }

    @Override
    public PriorityBlockingQueue<ScheduledJob> load(JobQueueData data) 
    {
        PriorityBlockingQueue<ScheduledJob> queue = new PriorityBlockingQueue<ScheduledJob>();
        String[] arr = FileReader.read(data.file);
        for (String job : arr)
        {
            ScheduledJob sJob = loadScheduledJob(job , data.alloy);
            if (sJob != null)
                queue.add(sJob);

        }
        return queue;
    }

    private ScheduledJob loadScheduledJob(String job, Alloy alloy) 
    {
        String[] args = job.split(SEPARATION_KEY);
        args = cleanArgs(args);
        String clazzS = args[0];
        if (clazzS.equalsIgnoreCase("RemindJob"))
            return buildRemindJob(args, alloy);
        return null;
    }

    private String[] cleanArgs(String[] args) 
    {
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
        sm.setFrom(getClass());
        sm.setMessage(outM);

        RemindJob rJob = new RemindJob(alloy, sm);
        ScheduledJob newSJob = EventManager.newScheduledJob(time , rJob);
        return newSJob;
        
    }
}
