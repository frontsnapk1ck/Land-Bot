package alloy.handler.command;

import java.io.File;
import java.io.IOException;

import alloy.gameobjects.Server;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import disterface.util.template.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import io.Saver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class EventHandler {

    public static void onGuildJoinEvent(Guild g) 
    {
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

        Saver.saveOverwrite(botS.getAbsolutePath(), loadBotSettingsArr(g));
    }

    public static void onMemberJoinEvent(Member m) 
    {
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
        Saver.saveOverwrite(acc.getAbsolutePath(), new String[] { "bal>" + s.getStartingBalance() });
        Saver.saveOverwrite(rank.getAbsolutePath(), new String[] { "0" });
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
                Server.BLACKLISTED_CHANNELS + ":", 
                Server.XP_COOLDOWN + ":4", 
                Server.ID + ":" + guildID,
                Server.MOD_LOG_CHANNEL + ":",
                Server.USER_LOG_CHANNEL + ":", 
                Server.MUTE_ROLE_ID + ":", 
                Server.BAN_APPEAL_LINK + ":" ,
            };

        return boSS;
    }

    public static void onGuildBan(Guild guild, User user, Sendable bot) 
    {
        Server s = AlloyUtil.loadServer(guild);

        if (s.getModLogChannel() != 0l)
            logBan(guild,bot,user,s.getModLogChannel());

        if (s.getBanAppealLink() != null && !s.getBanAppealLink().equalsIgnoreCase("none"))
            dmBanned(user,s.getBanAppealLink() , bot);
	}

    private static void dmBanned(User user, String banAppealLink, Sendable bot) 
    {
        PrivateChannel channel = user.openPrivateChannel().complete();
        Template t = Templates.banAppeal(banAppealLink);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom("EventHandler");
        bot.send(sm);
    }

    private static void logBan(Guild guild, Sendable bot, User user, long l) 
    {
        Template t = Templates.logBan(guild , user);
        TextChannel channel = guild.getTextChannelById(l);

        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom("EventHandler");
        bot.send(sm);
    }

}
