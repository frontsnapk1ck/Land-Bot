package alloy.main;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.security.auth.login.LoginException;

import alloy.builder.loaders.ServerLoaderText;
import alloy.event.AlloyLogger;
import alloy.event.JDAEvents;
import alloy.gameobjects.Server;
import alloy.handler.CommandHandler;
import alloy.handler.RankHandeler;
import alloy.input.console.ConsoleInput;
import alloy.input.discord.AlloyInput;
import alloy.main.handler.AlloyHandler;
import alloy.main.handler.ConsoleHandler;
import alloy.main.handler.CooldownHandler;
import alloy.utility.discord.AlloyUtil;
import botcord.event.DebugListener;
import io.FileReader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import utility.event.Job;

public class Alloy implements Sendable, Moderator, Loggable, Queueable, 
                                ConsoleHandler, AlloyHandler, CooldownHandler,
                                UncaughtExceptionHandler 
{

    public static final AlloyLogger LOGGER = new AlloyLogger();

    public static long startupTimeStamp;

    private JDA jda;
    private AlloyData data;
    private String mentionMeAlias;
    private String mentionMe;

    private boolean started;

    public Alloy() 
    {
        Alloy.startupTimeStamp = System.currentTimeMillis();
        Thread.currentThread().setUncaughtExceptionHandler(this);
        boolean startedL = false;
        while (!startedL) {
            try {
                start();
                startedL = true;
                makeMentions();
            } catch (LoginException e) {
                e.printStackTrace();
                cooldown(5);
            }
        }
        data = new AlloyData(jda, this, this);
    }

    private void makeMentions() {
        this.mentionMe = "<@" + this.jda.getSelfUser().getId() + ">";
        this.mentionMeAlias = "<@!" + this.jda.getSelfUser().getId() + ">";
    }

    private void start() throws LoginException {
        String key = loadKey();
        jda = JDABuilder.createDefault(key)
                        .enableIntents(GatewayIntent.GUILD_MEMBERS)
                        .setMemberCachePolicy(MemberCachePolicy.ALL)
                        .setChunkingFilter(ChunkingFilter.ALL)
                        .build();
        jda.addEventListener(new JDAEvents(this));
    }

    private String loadKey() {
        String[] keyA = FileReader.read(AlloyUtil.KEY_FILE);
        return keyA[0];
    }

    public static long getStartupTimeStamp() 
    {
        return startupTimeStamp;
    }

    private void cooldown(int seconds) 
    {
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

        if (author == null || author.isBot())
            return;

        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(in.getGuild()));

        in.setBot(this);
        in.setServer(s);

        RankHandeler.addXP(in.getData());
        String message = in.getMessage();
        if (CommandHandler.isCommand(message, mentionMe, mentionMeAlias, s))
        {
            in = CommandHandler.removePrefix(in, s);
            CommandHandler.process(in);
        }
    }

    @Override
    public void handlePrivateMessage(PrivateChannel channel, User author, Message message) 
    {
        if (!this.started)
            return;
        // TODO
    }

    @Override
    public void handleConsoleMessage(ConsoleInput in) {
        CommandHandler.process(in);
    }

    @Override
    public JDA getJDA() {
        return jda;
    }

    public static String getGuildPath(Guild guild) {
        return AlloyUtil.SERVERS_PATH + AlloyUtil.SUB + guild.getId();
    }

    public void updateActivty() {
        int size = jda.getGuilds().size();
        String message = "Ping for Help | " + size + " servers";

        jda.getPresence().setActivity(Activity.playing(message));
    }

    @Override
    public TextChannel getModLogChannel(long gid) {
        return data.getModLogChannel(gid);
    }

    @Override
    public void send(SendableMessage message) {
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

        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>(){
            @Override
            public void accept(ErrorResponseException t) 
            {
                LOGGER.warn(from, t.getMessage());
            }
            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.CANNOT_SEND_TO_USER , consumer );

        try 
        {
            channel.sendMessage(messageM).queue(null , handler);
        }
        catch (InsufficientPermissionException e) 
        {
            LOGGER.warn(from, e.getMessage());
        }
    }

    private void sendS(SendableMessage message) {
        MessageChannel channel = message.getChannel();
        String messageS = message.getMessageS();
        String from = message.getFrom();

        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>(){
            @Override
            public void accept(ErrorResponseException t) 
            {
                LOGGER.warn(from, t.getMessage());
            }
            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.CANNOT_SEND_TO_USER , consumer );

        try 
        {
            channel.sendMessage(messageS).queue(null , handler);
        }
        catch (InsufficientPermissionException e) 
        {
            LOGGER.warn(from, e.getMessage());
        }

    }

    private void sendE(SendableMessage message) 
    {
        MessageChannel channel = message.getChannel();
        MessageEmbed messageE = message.getMessageE();
        String from = message.getFrom();

        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>(){
            @Override
            public void accept(ErrorResponseException t) 
            {
                LOGGER.warn(from, t.getMessage());
            }
            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.CANNOT_SEND_TO_USER , consumer );

        try 
        {
            channel.sendMessage(messageE).queue(null , handler);
        }
        catch (InsufficientPermissionException e) 
        {
            LOGGER.warn(from, e.getMessage());
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

        try {
            return channel.sendMessage(messageM);
        } catch (Exception e) {
            LOGGER.warn(from, e.getMessage());
        }
        return null;
    }

    private MessageAction getActionS(SendableMessage message) 
    {
        MessageChannel channel = message.getChannel();
        String messageS = message.getMessageS();
        String from = message.getFrom();

        try {
            return channel.sendMessage(messageS);
        } catch (Exception e) {
            LOGGER.warn(from, e.getMessage());
        }
        return null;
    }

    private MessageAction getActionE(SendableMessage message) 
    {
        MessageChannel channel = message.getChannel();
        MessageEmbed messageE = message.getMessageE();
        String from = message.getFrom();

        try {
            return channel.sendMessage(messageE);
        } catch (Exception e) {
            LOGGER.warn(from, e.getMessage());
        }
        return null;
    }

    @Override
    public void addCooldownUser(Member m) {
        Guild g = m.getGuild();
        Map<Long, List<Long>> cooldownUsers = this.data.getCooldownUsers();
        if (!cooldownUsers.containsKey(g.getIdLong()))
            cooldownUsers.put(g.getIdLong(), new ArrayList<Long>());
        cooldownUsers.get(g.getIdLong()).add(m.getIdLong());
    }

    @Override
    public boolean removeCooldownUser(Member m) {
        Guild g = m.getGuild();
        Map<Long, List<Long>> cooldownUsers = this.data.getCooldownUsers();
        List<Long> list = cooldownUsers.get(g.getIdLong());
        boolean removed = list.remove(m.getIdLong());
        return removed;
    }

    @Override
    public List<Long> getCooldownUsers(Guild g) {
        return this.data.getCooldownUsers(g);
    }

    public void update() 
    {
        this.started = true;
        this.updateActivty();
        this.data.update();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) 
    {
        LOGGER.error(t.getName(), e);
        t.run();
    }

    @Override
    public boolean removeXpCooldownUser(Member m) {
        Guild g = m.getGuild();
        Map<Long, List<Long>> cooldownUsers = this.data.getXpCooldownUsers();
        List<Long> list = cooldownUsers.get(g.getIdLong());
        boolean removed = list.remove(m.getIdLong());
        return removed;
    }

    @Override
    public List<Long> getXpCooldownUsers(Guild g) {
        return this.data.getXpCooldownUsers(g);
    }

    @Override
    public void addXpCooldownUser(Member m) 
    {
        Guild g = m.getGuild();
        Map<Long, List<Long>> cooldownUsers = this.data.getXpCooldownUsers();
        if (!cooldownUsers.containsKey(g.getIdLong()))
            cooldownUsers.put(g.getIdLong(), new ArrayList<Long>());
        cooldownUsers.get(g.getIdLong()).add(m.getIdLong());

    }

    @Override
    public void queueIn(Job action, long offset) 
    {
        this.data.queueIn( action , offset );
    }

    @Override
    public void queue(Job action) 
    {
        this.data.queue(action);
    }

    public void setDebugListener(DebugListener debugListener) 
    {
        LOGGER.setListener(debugListener);
	}

    @Override
    public void guildCountUpdate() 
    {
        updateActivty();
    }

}