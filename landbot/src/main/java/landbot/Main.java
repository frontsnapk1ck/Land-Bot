package landbot;

import javax.security.auth.login.LoginException;

import landbot.commands.AdminCommands;
import landbot.commands.BuildingCommands;
import landbot.commands.Commands;
import landbot.commands.SpamCommands;
import landbot.commands.UpdateManager;
import landbot.io.FileReader;
import landbot.utility.ServerJoinListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Main implements ServerJoinListener {

    private static JDA jda;
    private static String key;

    private static Main main;

    private AdminCommands adminCommands;
    private SpamCommands spamCommands;
    private BuildingCommands buildingCommands;
    private Commands commands;
    private UpdateManager updateManager;

    public static void main(String[] args) {
        main = new Main();
        main.start();
    }

    private void start() {
        loadKey();
        try {
            jda = JDABuilder.createDefault(key).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        jda.getPresence().setStatus(OnlineStatus.ONLINE);

        this.updateManager = new UpdateManager();
        this.commands = new Commands();
        this.buildingCommands = new BuildingCommands();
        this.adminCommands = new AdminCommands();
        this.spamCommands = new SpamCommands();

        this.updateManager.setListener(this);

        jda.addEventListener(this.updateManager);
        jda.addEventListener(this.commands);
        jda.addEventListener(this.buildingCommands);
        jda.addEventListener(this.adminCommands);
        jda.addEventListener(this.spamCommands);

        this.buildChecker();

    }

    private void buildChecker() {
        Runnable r = new Runnable() 
        {
            @Override
            public void run() 
            {
                cooldown(1000);
                while (true)
                {
                    updateActivty();
                    cooldown(
                        10 * // mins
                        60 * // secs
                        1000 // millis
                    );
                }
            }

            private void cooldown(int millis) {
                try 
                {
                    Thread.sleep(millis);
                } 
                catch (InterruptedException e) 
                {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread( r , "Server Count Checker");
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
    }
    
}
