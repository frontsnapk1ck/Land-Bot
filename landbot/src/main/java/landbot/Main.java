package landbot;

import javax.security.auth.login.LoginException;

import landbot.commands.AdminCommands;
import landbot.commands.BuildingCommands;
import landbot.commands.Commands;
import landbot.commands.SpamCommands;
import landbot.commands.UpdateManager;
import landbot.io.FileReader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;

public class Main {

    private static JDA jda;
    private static String key;

    private static Main main;

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

        jda.addEventListener(new UpdateManager());
        jda.addEventListener(new Commands());
        jda.addEventListener(new BuildingCommands());
        jda.addEventListener(new AdminCommands());
        jda.addEventListener(new SpamCommands());

    }

    private static void loadKey() {
        String[] keyA = FileReader.read("landbot\\res\\key.token");
        key = keyA[0];
    }
    
}
