package frontsnapk1ck.alloy.main.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import frontsnapk1ck.alloy.audio.GuildMusicManager;
import frontsnapk1ck.alloy.event.DebugListener;
import frontsnapk1ck.alloy.event.DiscordInterface;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.handler.command.AudioHandler;
import frontsnapk1ck.alloy.input.console.Console;
import frontsnapk1ck.alloy.io.loader.JobQueueLoaderText;
import frontsnapk1ck.alloy.io.loader.util.JobQueueData;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.job.AlloyEventHandler;
import frontsnapk1ck.io.Saver;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import frontsnapk1ck.utility.event.EventManager.ScheduledJob;
import frontsnapk1ck.utility.event.Job;
import frontsnapk1ck.utility.event.Worker;

public class AlloyData {

    protected Map<Long, TextChannel> modLogs = new HashMap<Long, TextChannel>();
    protected Map<Long, List<Long>> cooldownUsers = new HashMap<Long, List<Long>>();
    protected Map<Long, List<Long>> xpCooldownUsers = new HashMap<Long, List<Long>>();
    
    private List<Job> tmpQueue = new ArrayList<Job>();
    private Console console = new Console();
    private AlloyEventHandler eventManger;
    private BigInteger messages;
    
    //audio
    private AudioPlayerManager playerManager;
    private Map<Long, GuildMusicManager> musicManagers;  
    
    private JDA jda;
    private DiscordInterface discordInterface;
    
    private Alloy alloy;
    
    public AlloyData(JDA jda, Alloy alloy) 
    {
        this.jda = jda;
        this.alloy = alloy;
        this.messages = BigInteger.ZERO;
        console.setHandler(alloy);
        configHandlers();
    }
    
    private void configHandlers() 
    {
        configAudio();
    }

    private void configAudio() 
    {
        this.musicManagers = new HashMap<Long, GuildMusicManager>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);

        AudioHandler.setAudible(alloy);
    }

    private AlloyEventHandler loadEventManager() 
    {
        AlloyEventHandler handler = new AlloyEventHandler();
        JobQueueLoaderText jqlt = new JobQueueLoaderText();
        JobQueueData data = new JobQueueData(this.alloy, AlloyUtil.EVENT_FILE);
        PriorityBlockingQueue<ScheduledJob> jobQueue = jqlt.load(data);
        if (jobQueue.size() == 0)
            Alloy.LOGGER.info("AlloyData", "there was nothing to load in the queue");
        else
            Alloy.LOGGER.info("AlloyData", "loaded the queue, there are " + jobQueue.size() + " events");
        handler.setJobQueue(jobQueue);
        Saver.clear(AlloyUtil.EVENT_FILE);

        for (Job j : tmpQueue)
            handler.queue(j);

        return handler;
    }

    private Map<Long, TextChannel> loadModLogs() 
    {
        Map<Long , TextChannel> map = new HashMap<Long , TextChannel>();
       
        List<Server> servers = AlloyUtil.loadAllServers();

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
        this.console.setHandler(alloy);
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
        System.out.println("AlloyData.updateCooldownUsers()");
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
        if( this.eventManger == null)
            this.tmpQueue.add(action);
        else
            this.eventManger.queue(action);
	}

    public void queueIn(Job action, long offset) 
    {
        if (this.eventManger == null)
            return;
        this.eventManger.queueIn(action, offset);
	}

    public AlloyEventHandler getEventHandler() 
    {
		return this.eventManger;
	}

    public boolean unQueue(Job job) 
    {
        if( this.eventManger == null)
            return this.tmpQueue.remove(job);
		return this.eventManger.unQueue(job);
	}

    public void makeJobs() 
    {
        // temporarily disabled 
        // Job j = new DayJob( this.jda );
        // this.eventManger.queueIn( j, 86400000L );
	}

    public DebugListener getDiscordInterface() 
    {
		return this.discordInterface;
	}

    public void setDiscordInterface(DiscordInterface discordInterface) 
    {
        this.discordInterface = discordInterface;
	}

	public void addGuildMap(Guild g) 
    {
        this.cooldownUsers.put(g.getIdLong(),new ArrayList<Long>());
        this.xpCooldownUsers.put(g.getIdLong(),new ArrayList<Long>());
	}

	public List<Worker> getWorkers() 
    {
        return this.eventManger.getWorkers();
	}

    public synchronized GuildMusicManager getGuildMusicManager(Guild g)
    {
        long guildId = Long.parseLong(g.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);
    
        if (musicManager == null) 
        {
            musicManager = new GuildMusicManager(playerManager , g);
            musicManagers.put(guildId, musicManager);
        }
    
        g.getAudioManager().setSendingHandler(musicManager.getSendHandler());
    
        return musicManager;
    }

    public AudioPlayerManager getPlayerManager() 
    {
        return playerManager;
    }

    public void messageReceived() 
    {
        this.messages = this.messages.add(BigInteger.ONE);
    }

    public BigInteger getMessages() 
    {
        return messages;
    }

}
