package alloy.handler;

import java.util.List;

import alloy.gameobjects.RankUp;
import alloy.gameobjects.Server;
import alloy.gameobjects.player.Rank;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.DisUtil;
import alloy.utility.settings.RankUpSettings;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import utility.StringUtil;
import utility.Util;

public class RankupHandler {

    public static boolean hasRole(String[] args, Guild g) {
        if (args.length > 3)
            return DisUtil.isRole(args[2], g);
        return false;
    }

    public static void addRankupRole(String[] args, Sendable bot, TextChannel channel, Guild g) {
        Server s = AlloyUtil.loadServer(g);
        boolean vNum = Util.validInt(args[1]);
        boolean vRole = DisUtil.isRole(args[2], g);

        if (!vNum) {
            Template t = Templates.invalidNumberFormat(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RankupHandler");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!vRole) {
            Template t = Templates.invalidRole(args[2]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RankupHandler");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String role = DisUtil.trimMention(args[2]);
        long id = Long.parseLong(role);
        int level = Integer.parseInt(args[1]);

        if (isDuplicateRankup(level, s)) {
            Template t = Templates.duplicateRankup(level);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RankupHandler");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String message = StringUtil.joinStrings(args, 3);

        RankUpSettings settings = new RankUpSettings();
        settings.setId(id).setLevel(level).setMessage(message);

        RankUp ru = new RankUp(settings);
        s.addRankUp(ru);

        rankupAddSuccess(bot, channel, ru);
    }

    private static boolean isDuplicateRankup(int level, Server s) {
        for (RankUp ru : s.getRankups()) {
            if (ru.getLevel() == level)
                return true;
        }
        return false;
    }

    public static void addRankup(String[] args, Sendable bot, TextChannel channel, Guild g) {
        Server s = AlloyUtil.loadServer(g);
        boolean vNum = Util.validInt(args[1]);

        if (!vNum) {
            Template t = Templates.invalidNumberFormat(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RankupHandler");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        int level = Integer.parseInt(args[1]);

        if (isDuplicateRankup(level, s)) {
            Template t = Templates.duplicateRankup(level);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RankupHandler");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String message = StringUtil.joinStrings(args, 2);

        RankUpSettings settings = new RankUpSettings();
        settings.setLevel(level).setMessage(message);

        RankUp ru = new RankUp(settings);
        s.addRankUp(ru);

        rankupAddSuccess(bot, channel, ru);

    }

    private static void rankupAddSuccess(Sendable bot, TextChannel channel, RankUp ru) {
        Template t = Templates.rankupAddSuccess(ru);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("RankupHandler");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        return;
    }

    public static void removeRankup(int level, Guild g, TextChannel channel, Sendable bot) {
        Server s = AlloyUtil.loadServer(g);
        List<RankUp> rankups = s.getRankups();
        RankUp toRm = null;

        for (RankUp rankUp : rankups) {
            if (rankUp.getLevel() == level)
                toRm = rankUp;
        }

        if (toRm != null) {
            s.removeRankUp(toRm);
            Template t = Templates.rankupRemoveSuccess(toRm);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RankupHandler");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        else {
            Template t = Templates.levelNotFound("" + level);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RankupHandler");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

    }

    public static boolean containsLevel(Guild g, int level) {
        Server s = AlloyUtil.loadServer(g);
        for (RankUp ru : s.getRankups()) {
            if (ru.getLevel() == level)
                return true;
        }
        return false;
    }

    public static List<RankUp> loadRankups(Guild g) {
        Server s = AlloyUtil.loadServer(g);
        return s.getRankups();
    }

    public static Rank getRank(int level) {
        List<Rank> ranks = AlloyUtil.loadAllGlobalRanks();
        for (Rank rank : ranks) {
            if (rank.getLevel() == level)
                return rank;
        }
        return null;
    }

}
