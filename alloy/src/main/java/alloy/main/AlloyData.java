package alloy.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

import alloy.command.util.CommandInfoLoader;
import alloy.gameobjects.Server;
import alloy.input.console.Console;
import alloy.io.loader.JobQueueLoaderText;
import alloy.io.loader.ServerLoaderText;
import alloy.io.loader.util.JobQueueData;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.job.AlloyEventHandler;
import alloy.utility.job.jobs.SpamRunnable;
import io.Saver;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import utility.event.EventManager.ScheduledJob;
import utility.event.Job;

public class AlloyData {

    private Map<Long, TextChannel> modLogs = new HashMap<Long, TextChannel>();
    protected Map<Long, List<Long>> cooldownUsers = new HashMap<Long, List<Long>>();
    protected Map<Long, List<Long>> xpCooldownUsers = new HashMap<Long, List<Long>>();
    private Console console = new Console();
    private AlloyEventHandler eventManger;
    private Alloy alloy;

    private JDA jda;

    public AlloyData(JDA jda, Alloy alloy) 
    {
        this.jda = jda;
        this.alloy = alloy;
        console.setHandler(alloy);
        console.setEHandler(alloy);
        CommandInfoLoader.loadInfo();
    }

    private AlloyEventHandler loadEventManager() 
    {
        AlloyEventHandler handler = new AlloyEventHandler();
        JobQueueLoaderText jqlt = new JobQueueLoaderText();
        JobQueueData data = new JobQueueData(this.alloy, AlloyUtil.EVENT_FILE);
        PriorityBlockingQueue<ScheduledJob> jobQueue = jqlt.load(data);
        if (jobQueue.size() == 0)
            Alloy.LOGGER.info("AlloyData", "there was nothign to load in the queue");
        else
            Alloy.LOGGER.info("AlloyData", "loaded the queue, there are " + jobQueue.size() + " events");
        handler.setJobQueue(jobQueue);
        Saver.clear(AlloyUtil.EVENT_FILE);
        return handler;
    }

    private Map<Long, TextChannel> loadModLogs() 
    {
        Map<Long , TextChannel> map = new HashMap<Long , TextChannel>();

        ServerLoaderText slt = new ServerLoaderText();
        String path = AlloyUtil.SERVERS_PATH;
        List<Server> servers = slt.loadALl(path);

        for (Server server : servers) 
        {
            long id = server.getId();
            Guild g = AlloyUtil.getGuild(server , jda);

            if (g == null)
                continue;

            long mid = server.getModLogChannel();
            TextChannel tc = g.getTextChannelById(mid);
            map.put(id, tc);
        }

        return map;
    }

    public TextChannel getModLogChannel(Long gid) 
    {
        loadModLogs();
      	return this.modLogs.get(gid);
    }

    public Map<Long, List<Long>> getCooldownUsers() 
    {
        return cooldownUsers;
    }
    
    public Map<Long, List<Long>> getXpCooldownUsers()
    {
        return xpCooldownUsers;
    }

    public List<Long> getCooldownUsers(Guild g) 
    {
		return this.cooldownUsers.get(g.getIdLong());
    }

    public List<Long> getXpCooldownUsers(Guild g)
    {
        return xpCooldownUsers.get(g.getIdLong() );
    }
    
    public void update()
    {
        this.modLogs = loadModLogs();
        this.eventManger = loadEventManager();
        this.updateCooldownUsers();
        this.updateXpCooldownUsers();
    }

    private void updateXpCooldownUsers() 
    {
        List<Guild> guilds = this.jda.getGuilds();
        for (Guild guild : guilds) 
        {
            long id = guild.getIdLong();
            if (!this.xpCooldownUsers.containsKey(id))
                this.xpCooldownUsers.put(id, new ArrayList<Long>());
        }
    }

    private void updateCooldownUsers() 
    {
        List<Guild> guilds = this.jda.getGuilds();
        for (Guild guild : guilds) 
        {
            long id = guild.getIdLong();
            if (!this.cooldownUsers.containsKey(id))
                this.cooldownUsers.put(id, new ArrayList<Long>());
            
        }
    }

    public void queue(Job action) 
    {
        if (action instanceof SpamRunnable )
            this.eventManger.queue((SpamRunnable) action);
        else
            this.eventManger.queue(action);
	}

    public void queueIn(Job action, long offset) 
    {
        this.eventManger.queueIn(action, offset);
	}

    public AlloyEventHandler getEventHandler() 
    {
		return this.eventManger;
	}

}
