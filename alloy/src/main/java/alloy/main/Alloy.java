package alloy.main;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionException;

import javax.security.auth.login.LoginException;

import alloy.event.AlloyLogger;
import alloy.event.DebugListener;
import alloy.event.DiscordInterface;
import alloy.event.JDAEvents;
import alloy.gameobjects.Server;
import alloy.handler.CommandHandler;
import alloy.handler.RankHandler;
import alloy.input.console.ConsoleInput;
import alloy.input.discord.AlloyInput;
import alloy.main.intefs.Loggable;
import alloy.main.intefs.Moderator;
import alloy.main.intefs.Queueable;
import alloy.main.intefs.Sendable;
import alloy.main.intefs.handler.AlloyHandler;
import alloy.main.intefs.handler.ConsoleHandler;
import alloy.main.intefs.handler.CooldownHandler;
import alloy.main.util.AlloyData;
import alloy.main.util.SendableMessage;
import alloy.main.util.SendableMessage.AttachedFile;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.runnable.AlloyShutdownHook;
import io.FileReader;
import io.Saver;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import utility.event.EventManager.ScheduledJob;
import utility.event.Job;
import utility.logger.Level;

public class Alloy implements Sendable, Moderator, Loggable, Queueable, ConsoleHandler, AlloyHandler, CooldownHandler, UncaughtExceptionHandler {

    public static final AlloyLogger LOGGER = new AlloyLogger();

    public static long startupTimeStamp;
    private static JDA jda;
    
    private String mentionMeAlias;
    private String mentionMe;
    private boolean started;
    
    private AlloyData data;    

    public Alloy() 
    {
        boolean startedL = false;
        while (!startedL) 
        {
            try 
            {
                start();
                config();
                startedL = true;
                makeMentions();
            }
            catch (LoginException e) 
            {
                e.printStackTrace();
                cooldown(5);
            }
        }
    }

    private void config() 
    {
        configThread();
        data = new AlloyData(jda, this);
        AlloyUtil.loadCache(this);
        Alloy.startupTimeStamp = System.currentTimeMillis();
    }



    private void configThread() 
    {
        Runtime r = Runtime.getRuntime();
        AlloyShutdownHook hook = new AlloyShutdownHook(this);
        r.addShutdownHook(hook);
    }

    private void makeMentions() {
        this.mentionMe = "<@" + jda.getSelfUser().getId() + ">";
        this.mentionMeAlias = "<@!" + jda.getSelfUser().getId() + ">";
    }

    private void start() throws LoginException 
    {
        String key = loadKey();
        jda = JDABuilder.createDefault(key).enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL).setChunkingFilter(ChunkingFilter.ALL).build();
        jda.addEventListener(new JDAEvents(this));
    }

    private String loadKey() {
        String[] keyA = FileReader.read(AlloyUtil.KEY_FILE);
        return keyA[0];
    }

    public static long getStartupTimeStamp() {
        return startupTimeStamp;
    }

    private void cooldown(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        }

        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(AlloyInput in) 
    {
        if (!this.started)
            return;

        User author = in.getUser();

        boolean ignore = author == null || author.isBot() || AlloyUtil.isBlackListed(author);
        if (ignore)
            return;

        Server s = AlloyUtil.loadServer(in.getGuild());

        in.setBot(this);
        in.setServer(s);

        RankHandler.addXP(in.getData());
        String message = in.getMessage();
        if (CommandHandler.isCommand(message, mentionMe, mentionMeAlias, s)) 
        {
            in = CommandHandler.removePrefix(in, s);
            CommandHandler.process(in);
        }
    }

    @Override
    public void handleConsoleMessage(ConsoleInput in) 
    {
        in.getData().setQueue(this);
        in.getData().setSendable(this);
        CommandHandler.process(in);
    }

    @Override
    public JDA getJDA() {
        return jda;
    }

    public static String getGuildPath(Guild guild) {
        return AlloyUtil.SERVERS_PATH + AlloyUtil.SUB + guild.getId();
    }

    public void updateActivity() {
        int size = jda.getGuilds().size();
        String message = "Ping for Help | " + size + " servers";

        jda.getPresence().setActivity(Activity.playing(message));
    }

    @Override
    public TextChannel getModLogChannel(long gid) {
        return data.getModLogChannel(gid);
    }

    @Override
    public void send(SendableMessage message) 
    {
        if (message.hasMessageE())
            sendE(message);
        else if (message.hasMessageS())
            sendS(message);
        else if (message.hasMessage())
            sendM(message);
    }

    private void sendM(SendableMessage message) 
    {
        MessageChannel channel = message.getChannel();
        Message messageM = message.getMessage();
        String from = message.getFrom();

        try 
        {
            List<AttachedFile> files = message.getFiles();
            MessageAction action = channel.sendMessage(messageM);
            for (AttachedFile attFile : files)
                action.addFile(attFile.file);
            Message m = action.complete();
            for (AttachedFile attFile : files) 
            {
                if (!attFile.save)
                    Saver.deleteFile(attFile.file.getAbsolutePath());
            }
            message.setSent(m);
        }
        catch (InsufficientPermissionException | ErrorResponseException e) 
        {
            LOGGER.warn(from, e.getMessage());
        }
        catch (RejectedExecutionException ignored){
        }
    }

    private void sendS(SendableMessage message) 
    {
        MessageChannel channel = message.getChannel();
        String messageS = message.getMessageS();
        String from = message.getFrom();

        try 
        {
            List<AttachedFile> files = message.getFiles();
            MessageAction action = channel.sendMessage(messageS);
            for (AttachedFile attFile : files)
                action.addFile(attFile.file);
            Message m = action.complete();
            for (AttachedFile attFile : files) 
            {
                if (!attFile.save)
                    Saver.deleteFile(attFile.file.getAbsolutePath());
            }
            message.setSent(m);
        }
        catch (InsufficientPermissionException | ErrorResponseException e) 
        {
            LOGGER.warn(from, e.getMessage());
        }
        catch (RejectedExecutionException ignored){
        }

    }

    private void sendE(SendableMessage message) 
    {
        MessageChannel channel = message.getChannel();
        MessageEmbed messageE = message.getMessageE();
        String from = message.getFrom();

        try 
        {
            List<AttachedFile> files = message.getFiles();
            MessageAction action = channel.sendMessage(messageE);
            for (AttachedFile attFile : files)
                action.addFile(attFile.file);
            Message m = action.complete();
            for (AttachedFile attFile : files) 
            {
                if (!attFile.save)
                    Saver.deleteFile(attFile.file.getAbsolutePath());
            }
            message.setSent(m);
        }
        catch (InsufficientPermissionException | ErrorResponseException e) 
        {
            LOGGER.warn(from, e.getMessage());
        }
        catch (RejectedExecutionException ignored){
        }
    }

    @Override
    public MessageAction getAction(SendableMessage message) 
    {
        if (message.hasMessageE())
            return getActionE(message);
        else if (message.hasMessageS())
            return getActionS(message);
        else if (message.hasMessage())
            return getActionM(message);
        return null;
    }

    private MessageAction getActionM(SendableMessage message) 
    {
        MessageChannel channel = message.getChannel();
        Message messageM = message.getMessage();
        String from = message.getFrom();

        try 
        {
            List<AttachedFile> files = message.getFiles();
            MessageAction action = channel.sendMessage(messageM);
            for (AttachedFile file : files)
                action.addFile(file.file);
            return action;
        }
        catch (Exception e) 
        {
            LOGGER.warn(from, e.getMessage());
        }
        return null;
    }

    private MessageAction getActionS(SendableMessage message) 
    {
        MessageChannel channel = message.getChannel();
        String messageS = message.getMessageS();
        String from = message.getFrom();

        try 
        {
            List<AttachedFile> files = message.getFiles();
            MessageAction action = channel.sendMessage(messageS);
            for (AttachedFile file : files)
                action.addFile(file.file);
            return action;
        }
        catch (Exception e) 
        {
            LOGGER.warn(from, e.getMessage());
        }
        return null;
    }

    private MessageAction getActionE(SendableMessage message) {
        MessageChannel channel = message.getChannel();
        MessageEmbed messageE = message.getMessageE();
        String from = message.getFrom();

        try 
        {
            List<AttachedFile> files = message.getFiles();
            MessageAction action = channel.sendMessage(messageE);
            for (AttachedFile file : files)
                action.addFile(file.file);
            return action;
        }
        catch (Exception e) 
        {
            LOGGER.warn(from, e.getMessage());
        }
        return null;
    }

    @Override
    public void addCooldownUser(Member m) {
        Guild g = m.getGuild();
        Map<Long, List<Long>> cooldownUsers = data.getCooldownUsers();
        if (!cooldownUsers.containsKey(g.getIdLong()))
            cooldownUsers.put(g.getIdLong(), new ArrayList<Long>());
        cooldownUsers.get(g.getIdLong()).add(m.getIdLong());
    }

    @Override
    public boolean removeCooldownUser(Member m) {
        Guild g = m.getGuild();
        Map<Long, List<Long>> cooldownUsers = data.getCooldownUsers();
        List<Long> list = cooldownUsers.get(g.getIdLong());
        boolean removed = list.remove(m.getIdLong());
        return removed;
    }

    @Override
    public List<Long> getCooldownUsers(Guild g) {
        return data.getCooldownUsers(g);
    }

    public void update() 
    {
        this.started = true;
        this.updateActivity();
        Thread.setDefaultUncaughtExceptionHandler(this);
        data.update();
        configDiscordInterface();
    }

    private void configDiscordInterface() 
    {
        final long ALLOY_ID = 771814337420460072L;

        final long ERROR_ID = 805626387100467210L;
        final long DEBUG_ID = 809178603345805352L;
        final long WARN_ID  = 809178564452417548L;
        final long INFO_ID  = 809178584635277332L;

        Guild g = jda.getGuildById(ALLOY_ID);

        Map<Level , TextChannel> logs = new HashMap<Level,TextChannel>();
        logs.put(Level.ERROR , g.getTextChannelById( ERROR_ID ));
        logs.put(Level.DEBUG , g.getTextChannelById( DEBUG_ID ));
        logs.put(Level.WARN  , g.getTextChannelById( WARN_ID  ));
        logs.put(Level.INFO  , g.getTextChannelById( INFO_ID  ));

        this.data.setDiscordInterface( new DiscordInterface(logs) );

        LOGGER.addListener(this.getInterfaceListener());
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) 
    {
        LOGGER.error(t.getName(), e);
        t.run();
        this.update();
    }

    @Override
    public boolean removeXpCooldownUser(Member m) {
        Guild g = m.getGuild();
        Map<Long, List<Long>> cooldownUsers = data.getXpCooldownUsers();
        List<Long> list = cooldownUsers.get(g.getIdLong());
        boolean removed = list.remove(m.getIdLong());
        return removed;
    }

    @Override
    public List<Long> getXpCooldownUsers(Guild g) {
        return data.getXpCooldownUsers(g);
    }

    @Override
    public void addXpCooldownUser(Member m) {
        Guild g = m.getGuild();
        Map<Long, List<Long>> cooldownUsers = data.getXpCooldownUsers();
        if (!cooldownUsers.containsKey(g.getIdLong()))
            cooldownUsers.put(g.getIdLong(), new ArrayList<Long>());
        cooldownUsers.get(g.getIdLong()).add(m.getIdLong());

    }

    @Override
    public void queueIn(Job action, long offset) {
        data.queueIn(action, offset);
    }

    @Override
    public void queue(Job action) {
        data.queue(action);
    }

    public void setDebugListener(DebugListener debugListener) 
    {
        LOGGER.addListener(debugListener);
    }

    @Override
    public void guildCountUpdate() {
        updateActivity();
    }

    @Override
    public PriorityBlockingQueue<ScheduledJob> getQueue() 
    {
        try
        {
            return data.getEventHandler().getJobQueue();
        }
        catch (Exception e)
        {
            return new PriorityBlockingQueue<ScheduledJob>();
        }
    }

    @Override
    public boolean unQueue(Job job) 
    {
        return this.data.unQueue(job);
    }

    public void finishInit() 
    {
        this.update();
        data.makeJobs();
	}

    public DebugListener getInterfaceListener() 
    {
		return data.getDiscordInterface();
	}

    @Override
    public void addGuildMap(Guild g) 
    {
        this.data.addGuildMap(g);
    }

}