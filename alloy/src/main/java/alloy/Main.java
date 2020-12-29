package alloy;

import java.lang.Thread.UncaughtExceptionHandler;

import javax.security.auth.login.LoginException;

import alloy.commands.AdminCommands;
import alloy.commands.BuildingCommands;
import alloy.commands.Commands;
import alloy.commands.RankCommands;
import alloy.commands.SpamCommands;
import alloy.commands.UpdateManager;
import alloy.handler.InputHandeler;
import alloy.main.InputListener;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.event.ConsoleInputListener;
import alloy.utility.event.ServerJoinListener;
import io.FileReader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Main implements ServerJoinListener , ConsoleInputListener, UncaughtExceptionHandler {

    private static JDA jda;
    private static String key;

    private static Main main;

    private AdminCommands adminCommands;
    private SpamCommands spamCommands;
    private BuildingCommands buildingCommands;
    private Commands commands;
    private UpdateManager updateManager;
    private RankCommands rankCommands;
    private InputListener inputListener;

    public static void main(String[] args) 
    {
        main = new Main();
        main.start();
    }

    private void start() 
    {
        loadKey();
        try {
            jda = JDABuilder.createDefault(key)
                            .enableIntents(GatewayIntent.GUILD_MEMBERS)
                            .setMemberCachePolicy(MemberCachePolicy.ALL)
                            .setChunkingFilter(ChunkingFilter.ALL)
                            .build();
            
            jda.getPresence().setActivity(Activity.playing("LOADING"));
        } catch (LoginException e) {
            e.printStackTrace();
        }

        jda.getPresence().setStatus(OnlineStatus.ONLINE);

        this.updateManager = new UpdateManager();
        this.commands = new Commands();
        this.buildingCommands = new BuildingCommands();
        this.adminCommands = new AdminCommands();
        this.spamCommands = new SpamCommands();
        this.rankCommands = new RankCommands();

        this.updateManager.setListener(this);

        jda.addEventListener(this.updateManager);
        jda.addEventListener(this.commands);
        jda.addEventListener(this.buildingCommands);
        jda.addEventListener(this.adminCommands);
        jda.addEventListener(this.spamCommands);
        jda.addEventListener(this.rankCommands);

        updateActivtyRunable();
        makeInputListener();

    }

    private void makeInputListener() 
    {
        this.inputListener = new InputListener();
        this.inputListener.addListener(this);
        Thread t = new Thread(this.inputListener , "Input Listener");
        t.setUncaughtExceptionHandler(this);
        
        t.start();
    }

    private void updateActivtyRunable() 
    {
        Runnable r = new Runnable() {
            @Override
            public void run() 
            {
                try 
                {
                    Thread.sleep(5000);
                } 
                catch (InterruptedException e) 
                {
                    e.printStackTrace();
                }
                updateActivty();
            }
        };

        Thread t = new Thread(r , "stupid wait for the server to update");
        t.setDaemon(true);
        t.start();
    }

    private void updateActivty() 
    {
        int size = jda.getGuilds().size();
        String message = "Ping for Help | " + size + " servers";

        this.adminCommands.setGuilds(jda.getGuilds());

        jda.getPresence().setActivity(Activity.playing(message));
    }

    private static void loadKey() 
    {
        String path = AlloyUtil.KEY_FILE;
        String[] keyA = FileReader.read(path);
        key = keyA[0];
    }

    @Override
    public void onServerJoin() 
    {
        this.updateActivty();
    }

    @Override
    public void onServerLeave() 
    {
        this.adminCommands.setGuilds(jda.getGuilds());

        this.updateActivty();
    }

    @Override
    public void onConsoleInput(String input) 
    {
        InputHandeler.handle( jda , input);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) 
    {
        System.err.println(t.getName() + " - error");
        e.printStackTrace();
    }
}
