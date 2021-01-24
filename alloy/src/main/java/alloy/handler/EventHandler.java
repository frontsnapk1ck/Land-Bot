package alloy.handler;

import java.io.File;
import java.io.IOException;

import alloy.gameobjects.Server;
import alloy.utility.discord.AlloyUtil;
import io.Saver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class EventHandler {

    public static void onGuildJoinEvent(Guild g) {
        String path = AlloyUtil.getGuildPath(g);

        File top = new File(path);
        File users = new File(path + AlloyUtil.USER_FOLDER);
        File settings = new File(path + AlloyUtil.SETTINGS_FOLDER);
        File cases = new File(path + AlloyUtil.CASE_FOLDER);

        top.mkdir();
        users.mkdir();
        settings.mkdir();
        cases.mkdir();

        File botS = new File(settings.getAbsolutePath() + AlloyUtil.SUB + AlloyUtil.BOT_SETTINGS_FILE);
        File buildingS = new File(settings.getAbsolutePath() + AlloyUtil.SUB + AlloyUtil.BUILDING_FILE);
        File rankUps = new File(settings.getAbsolutePath() + AlloyUtil.SUB + AlloyUtil.RANK_UP_SETTINGS_FILE);
        File workOps = new File(settings.getAbsolutePath() + AlloyUtil.SUB + AlloyUtil.WORK_OPTIONS_FILE);

        try {
            botS.createNewFile();
            buildingS.createNewFile();
            rankUps.createNewFile();
            workOps.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Saver.copyFrom(AlloyUtil.GLOBAL_BUILDINGS_PATH, buildingS.getAbsolutePath());
        Saver.copyFrom(AlloyUtil.GLOBAL_WORK_OPTIONS_PATH, workOps.getAbsolutePath());

        Saver.saveOverwite(botS.getAbsolutePath(), loadBotSettingsArr(g));
    }

    public static void onMemberJoinEvent(Member m) {
        Guild g = m.getGuild();

        String path = AlloyUtil.getGuildPath(g);
        path += AlloyUtil.USER_FOLDER + AlloyUtil.SUB;
        path += m.getId();
        File top = new File(path);
        File warn = new File(path + AlloyUtil.WARNINGS_FOLDER);
        File acc = new File(path + AlloyUtil.SUB + AlloyUtil.ACCOUNT_FILE);
        File build = new File(path + AlloyUtil.SUB + AlloyUtil.BUILDING_FILE);
        File rank = new File(path + AlloyUtil.SUB + AlloyUtil.RANK_FILE);

        top.mkdir();
        warn.mkdir();
        try {
            acc.createNewFile();
            build.createNewFile();
            rank.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Server s = AlloyUtil.loadServer(g);
        Saver.saveOverwite(acc.getAbsolutePath(), new String[] { "bal>" + s.getStartingBalance() });
        Saver.saveOverwite(rank.getAbsolutePath(), new String[] { "0" });
    }

    public static void onGuildLeaveEvent(Guild g) {
        String path = AlloyUtil.getGuildPath(g);
        Saver.deleteFiles(path);
    }

    public static void onMemberLeaveEvent(Member m) {
        Guild g = m.getGuild();

        String path = AlloyUtil.getGuildPath(g);
        path += AlloyUtil.USER_FOLDER + AlloyUtil.SUB;
        path += m.getId();

        try {
            Saver.deleteFiles(path);
        } catch (Exception e) 
        {
            System.err.println(path);
            System.err.println(e.getMessage());
        }
    }

    private static String[] loadBotSettingsArr(Guild g) {
        long guildID = g.getIdLong();
        long defaultChannel = g.getDefaultChannel().getIdLong();

        String[] boSS = { 
                Server.PREFIX + ":!", 
                Server.STARTING_BALANCE + ":1000", 
                Server.COOLDOWN + ":10",
                Server.ROLE_ASSIGN_ON_BUY + ":false", 
                Server.SPAM_CHANNEL + ":" + defaultChannel,
                Server.BALCKLISTED_CHANNENLS + ":", 
                Server.XP_COOLDOWN + ":4", 
                Server.ID + ":" + guildID,
                Server.MOD_LOG_CHANNEL + ":",
                Server.USER_LOG_CHANNEL + ":", 
                Server.MUTE_ROLE_ID + ":", 
            };

        return boSS;
    }

}
