package frontsnapk1ck.alloy.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionException;

import javax.security.auth.login.LoginException;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import frontsnapk1ck.alloy.audio.GuildMusicManager;
import frontsnapk1ck.alloy.command.util.CommandInfoLoader;
import frontsnapk1ck.alloy.event.AlloyLogger;
import frontsnapk1ck.alloy.event.DebugListener;
import frontsnapk1ck.alloy.event.DiscordInterface;
import frontsnapk1ck.alloy.event.JDAEvents;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.handler.command.FunHandler;
import frontsnapk1ck.alloy.handler.util.CommandHandler;
import frontsnapk1ck.alloy.input.console.ConsoleInput;
import frontsnapk1ck.alloy.input.discord.AlloyInput;
import frontsnapk1ck.alloy.main.intefs.util.AlloyIntefs;
import frontsnapk1ck.alloy.main.util.AlloyData;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.main.util.SendableMessage.AttachedFile;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.runnable.AlloyShutdownHook;
import frontsnapk1ck.io.FileReader;
import frontsnapk1ck.io.Saver;
import frontsnapk1ck.utility.event.EventManager.ScheduledJob;
import frontsnapk1ck.utility.event.Job;
import frontsnapk1ck.utility.event.Worker;
import frontsnapk1ck.utility.logger.Level;
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
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Alloy implements AlloyIntefs {

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
        AlloyUtil.loadCache();
        Alloy.startupTimeStamp = System.currentTimeMillis();
    }

    private void configThread()
    {
        Runtime r = Runtime.getRuntime();
        AlloyShutdownHook hook = new AlloyShutdownHook(this);
        r.addShutdownHook(hook);
    }

    private void makeMentions()
    {
        this.mentionMe = "<@" + jda.getSelfUser().getId() + ">";
        this.mentionMeAlias = "<@!" + jda.getSelfUser().getId() + ">";
    }

    private void start() throws LoginException 
    {
        String key = loadKey();
        jda = JDABuilder.createDefault(key)
                        .enableIntents(GatewayIntent.GUILD_MEMBERS)
                        .setMemberCachePolicy(MemberCachePolicy.ALL)
                        .setChunkingFilter(ChunkingFilter.ALL)
                        .build();
        jda.addEventListener(new JDAEvents(this));
        registerSlashCommands();
    }

    private void registerSlashCommands()
    {
        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(CommandInfoLoader.loadSlashCommands());
        commands.queue();
    }

    private String loadKey()
    {
        String[] keyA = FileReader.read(AlloyUtil.KEY_FILE);
        return keyA[0];
    }

    public static long getStartupTimeStamp()
    {
        return startupTimeStamp;
    }

    private void cooldown(int seconds)
    {
        try
        {
            Thread.sleep(seconds * 1000);
        }

        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(AlloyInput in) 
    {
        if (!this.started)
            return;

        User author = in.getUser();

        data.messageReceived();

        boolean ignore =    author == null || 
                            author.isBot() || 
                            AlloyUtil.isBlackListed(author);
        if (ignore)
            return;

        Server s = AlloyUtil.loadServer(in.getGuild());

        in.setBot(this);
        in.setServer(s);

        if (s.isLoaded())
            FunHandler.addXP(in.getData());

        String message = in.getMessage();

        boolean normal =    CommandHandler.isCommand(message, mentionMe, mentionMeAlias, s) &&
                            s.isLoaded();

        boolean warning =   CommandHandler.isCommand(message, mentionMe, mentionMeAlias, s) &&
                            !s.isLoaded();
        if (normal)
        {
            in = CommandHandler.removePrefix(in, s);
            CommandHandler.process(in);
        }
        else if (warning)
            CommandHandler.warnServerNotLoaded(in);
    }

    @Override
    public void handleConsoleMessage(ConsoleInput in)
    {
        in.getData().setQueue(this);
        in.getData().setSendable(this);
        in.getData().setBot(this);
        CommandHandler.process(in);
    }

    @Override
    public JDA getJDA() 
    {
        return jda;
    }

    public static String getGuildPath(Guild guild)
    {
        return AlloyUtil.SERVERS_PATH + AlloyUtil.SUB + guild.getId();
    }

    public void updateActivity()
    {
        int size = jda.getGuilds().size();
        String message = "Ping for Help | " + size + " servers";

        jda.getPresence().setActivity(Activity.playing(message));
    }

    @Override
    public TextChannel getModLogChannel(long gid)
    {
        return data.getModLogChannel(gid);
    }

    @Override
    public void edit(Message oldMsg, SendableMessage newMsg)
    {
        try
        {
            oldMsg.editMessage(newMsg.getMessage()).complete();
        } catch (Exception e)
        {
            MessageChannel channel = oldMsg.getChannel();

            String from = newMsg.getFrom();

            String info = "";
            if (channel instanceof TextChannel)
                info += "Guild: " + ((TextChannel) channel).getGuild().getName() + "\t";
            info += "Channel: " + channel.getName() + "\t";
            info += "Error Type: " + e.getClass().getSimpleName();

            LOGGER.warn(from, e.getMessage() + "\t" + info);
        }
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
            for (AttachedFile attFile : files) {
                if (!attFile.save)
                    Saver.deleteFile(attFile.file.getAbsolutePath());
            }
            message.setSent(m);
        }
        catch (InsufficientPermissionException | ErrorResponseException e) 
        {
            String info = "";
            if (channel instanceof TextChannel)
                info += "Guild: " + ((TextChannel) channel).getGuild().getName() + "\t";
            info += "Channel: " + channel.getName() + "\t";
            info += "Error Type: " + e.getClass().getSimpleName();
            LOGGER.warn(from, e.getMessage() + "\t" + info);
        }
        catch (RejectedExecutionException ignored) {
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
        catch (InsufficientPermissionException | ErrorResponseException e) {
            String info = "";
            if (channel instanceof TextChannel)
                info += "Guild: " + ((TextChannel) channel).getGuild().getName() + "\t";
            info += "Channel: " + channel.getName() + "\t";
            info += "Error Type: " + e.getClass().getSimpleName();
            LOGGER.warn(from, e.getMessage() + "\t" + info);
        }
        catch (IllegalArgumentException e)
        {
            String[] newLines = messageS.split("\n");
            List<String> outStrings = new ArrayList<String>();

            String out = "";
            for (int i = 0; i < newLines.length; i++) 
            {
                String tmp = out + newLines[i] + "\n";
                if (tmp.length() > Message.MAX_CONTENT_LENGTH)
                {
                    outStrings.add(out);
                    out = newLines[i];
                }
                else
                    out = tmp;
                
                if ( i == newLines.length - 1)
                    outStrings.add(out);
            }

            for (String part : outStrings) 
            {
                message.setMessage(part);
                send(message);
            }
        }
        catch (RejectedExecutionException ignored) {
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
            MessageAction action = channel.sendMessageEmbeds(messageE);
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
            String info = "";
            if (channel instanceof TextChannel)
                info += "Guild: " + ((TextChannel) channel).getGuild().getName() + "\t";
            info += "Channel: " + channel.getName() + "\t";
            info += "Error Type: " + e.getClass().getSimpleName();
            LOGGER.warn(from, e.getMessage() + "\t" + info);
        }
        catch (RejectedExecutionException ignored) {
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
            if (action == null)
                throw new NullPointerException("The Action is Null");
            return action;
        }
        catch (Exception e)
        {
            String info = "";
            if (channel instanceof TextChannel)
                info += "Guild: " + ((TextChannel) channel).getGuild().getName() + "\t";
            info += "Channel: " + channel.getName();
            info += "Channel: " + channel.getName() + "\t";
            info += "Error Type: " + e.getClass().getSimpleName();
            LOGGER.warn(from, e.getMessage() + "\t" + info);
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
            if (action == null)
                throw new NullPointerException("The Action is Null");
            return action;
        }
        catch (Exception e) 
        {
            String info = "";
            if (channel instanceof TextChannel)
                info += "Guild: " + ((TextChannel) channel).getGuild().getName() + "\t";
            info += "Channel: " + channel.getName();
            info += "Channel: " + channel.getName() + "\t";
            info += "Error Type: " + e.getClass().getSimpleName();
            LOGGER.warn(from, e.getMessage() + "\t" + info);
        }
        return null;
    }

    private MessageAction getActionE(SendableMessage message)
    {
        MessageChannel channel = message.getChannel();
        MessageEmbed messageE = message.getMessageE();
        String from = message.getFrom();

        try
        {
            List<AttachedFile> files = message.getFiles();
            MessageAction action = channel.sendMessageEmbeds(messageE);
            for (AttachedFile file : files)
                action.addFile(file.file);
            if (action == null)
                throw new NullPointerException("The Action is Null");
            return action;
        }
        catch (Exception e)
        {
            String info = "";
            if (channel instanceof TextChannel)
                info += "Guild: " + ((TextChannel) channel).getGuild().getName() + "\t";
            info += "Channel: " + channel.getName() + "\t";
            info += "Error Type: " + e.getClass().getSimpleName();
            LOGGER.warn(from, e.getMessage() + "\t" + info);
        }
        return null;
    }

    @Override
    public void addCooldownUser(Member m) 
    {
        Guild g = m.getGuild();
        Map<Long, List<Long>> cooldownUsers = data.getCooldownUsers();
        if (!cooldownUsers.containsKey(g.getIdLong()))
            cooldownUsers.put(g.getIdLong(), new ArrayList<Long>());
        cooldownUsers.get(g.getIdLong()).add(m.getIdLong());
    }

    @Override
    public boolean removeCooldownUser(Member m) 
    {
        Guild g = m.getGuild();
        Map<Long, List<Long>> cooldownUsers = data.getCooldownUsers();
        List<Long> list = cooldownUsers.get(g.getIdLong());
        boolean removed = list.remove(m.getIdLong());
        return removed;
    }

    @Override
    public List<Long> getCooldownUsers(Guild g) 
    {
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
        final long ALLOY_ID = 833530318790459412L;

        final long ERROR_ID = 805626387100467210L;
        final long DEBUG_ID = 809178603345805352L;
        final long WARN_ID = 809178564452417548L;
        final long INFO_ID = 809178584635277332L;

        Guild g = jda.getGuildById(ALLOY_ID);

        Map<Level, TextChannel> logs = new HashMap<Level, TextChannel>();
        logs.put(Level.ERROR, g.getTextChannelById(ERROR_ID));
        logs.put(Level.DEBUG, g.getTextChannelById(DEBUG_ID));
        logs.put(Level.WARN, g.getTextChannelById(WARN_ID));
        logs.put(Level.INFO, g.getTextChannelById(INFO_ID));

        this.data.setDiscordInterface(new DiscordInterface(logs));

        LOGGER.addListener(this.getInterfaceListener());
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) 
    {
        for (int i = 0; i < 4; i++)
        {
            StackTraceElement element = e.getStackTrace()[i];
            System.out.println("ERROR: " + element.getFileName() + "@" + element.getLineNumber());    
        }
        LOGGER.error(t.getName(), e);
        t.run();
        this.update();
    }

    @Override
    public boolean removeXpCooldownUser(Member m)
    {
        Guild g = m.getGuild();
        Map<Long, List<Long>> cooldownUsers = data.getXpCooldownUsers();
        List<Long> list = cooldownUsers.get(g.getIdLong());
        boolean removed = list.remove(m.getIdLong());
        return removed;
    }

    @Override
    public List<Long> getXpCooldownUsers(Guild g) 
    {
        return data.getXpCooldownUsers(g);
    }

    @Override
    public void addXpCooldownUser(Member m) 
    {
        Guild g = m.getGuild();
        Map<Long, List<Long>> cooldownUsers = data.getXpCooldownUsers();
        if (!cooldownUsers.containsKey(g.getIdLong()))
            cooldownUsers.put(g.getIdLong(), new ArrayList<Long>());
        cooldownUsers.get(g.getIdLong()).add(m.getIdLong());

    }

    @Override
    public void queueIn(Job action, long offset)
    {
        data.queueIn(action, offset);
    }

    @Override
    public void queue(Job action)
    {
        data.queue(action);
    }

    public void setDebugListener(DebugListener debugListener) 
    {
        LOGGER.addListener(debugListener);
    }

    @Override
    public void guildCountUpdate()
    {
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
        LOGGER.info("Alloy", mentionMeAlias);
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

    @Override
    public List<Worker> getWorkers() 
    {
        return data.getWorkers();
    }

    @Override
    public GuildMusicManager getGuildAudioPlayer(Guild g) 
    {
        return this.data.getGuildMusicManager(g);
    }

    @Override
    public AudioPlayerManager getPlayerManager() 
    {
        return this.data.getPlayerManager();
    }

    public AlloyData getData() 
    {
        return data;
    }

    @Override
    public void handleSlashMessage(AlloyInput in)
    {
        
    }

}