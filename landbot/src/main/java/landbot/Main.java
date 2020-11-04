package landbot;

import javax.security.auth.login.LoginException;

import landbot.commands.AdminCommands;
import landbot.commands.BuildingCommands;
import landbot.commands.Commands;
import landbot.commands.RankCommands;
import landbot.commands.SpamCommands;
import landbot.commands.UpdateManager;
import landbot.io.FileReader;
import landbot.utility.event.ServerJoinListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main implements ServerJoinListener {

    private static JDA jda;
    private static String key;

    private static Main main;

    private AdminCommands adminCommands;
    private SpamCommands spamCommands;
    private BuildingCommands buildingCommands;
    private Commands commands;
    private UpdateManager updateManager;
    private RankCommands rankCommands;

    public static void main(String[] args) {
        main = new Main();
        main.start();
    }

    private void start() {
        loadKey();
        try {
            jda = JDABuilder.createDefault(key)
                            .enableIntents(GatewayIntent.GUILD_MEMBERS)
                            .build();
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

    }

    private void updateActivtyRunable() {
        Runnable r = new Runnable() {
            @Override
            public void run() 
            {
                try 
                {
                    Thread.sleep(1000);
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

        jda.getPresence().setActivity(Activity.playing(message));
    }

    private static void loadKey() {
        String[] keyA = FileReader.read("landbot\\res\\token.token");
        key = keyA[0];
    }

    @Override
    public void onServerJoin() 
    {
        this.adminCommands.setGuilds(jda.getGuilds());

        this.updateActivty();
    }

    @Override
    public void onServerLeave() 
    {
        this.adminCommands.setGuilds(jda.getGuilds());

        this.updateActivty();
    }
}
