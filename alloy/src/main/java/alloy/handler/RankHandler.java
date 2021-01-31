package alloy.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import alloy.gameobjects.RankUp;
import alloy.gameobjects.Server;
import alloy.gameobjects.player.Player;
import alloy.gameobjects.player.Rank;
import alloy.input.discord.AlloyInputData;
import alloy.main.Queueable;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.main.handler.CooldownHandler;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.DisUtil;
import alloy.utility.job.jobs.AddUserXPCooldownJob;
import alloy.utility.job.jobs.RmUserXPCooldownJob;
import io.FileReader;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.event.Job;

public class RankHandler {

    public static final int MAX_LB_LENGTH = 10;

    public static void addXP(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        CooldownHandler handler = data.getCooldownHandler();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);
        Queueable q = data.getQueue();

        Server s = AlloyUtil.loadServer(g);
        Player p = AlloyUtil.loadPlayer(g, m);

        List<TextChannel> blacklisted = getBlacklistedChannels(g, s);
        if (channelIn(blacklisted, channel))
            return;

        long id = m.getIdLong();
        if (handler.getXpCooldownUsers(g) != null && handler.getXpCooldownUsers(g).contains(id))
            return;

        p.addXP(1);
        checkLevelUp(data);

        Job j = new AddUserXPCooldownJob(handler, m);
        q.queue(j);
        Job j2 = new RmUserXPCooldownJob(handler, m);
        q.queueIn(j2, s.getXPCooldown() * 1000l);
    }

    private static void checkLevelUp(AlloyInputData data) 
    {
        List<Rank> stock = AlloyUtil.loadAllGlobalRanks();

        Guild g = data.getGuild();
        User author = data.getUser();
        Member m = g.getMember(author);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        Player p = AlloyUtil.loadPlayer(g, m);

        for (Rank rank : stock) {
            int playerXP = p.getXP();
            int rankXP = rank.getTotalXP();
            if (playerXP == rankXP)
                announceRankUp(rank, m, true, bot, channel);
        }
    }

    private static boolean channelIn(List<TextChannel> blacklisted, TextChannel channel) {
        if (blacklisted == null)
            return false;

        for (TextChannel c : blacklisted) {
            if (c == channel)
                return true;
        }
        return false;
    }

    private static List<TextChannel> getBlacklistedChannels(Guild g, Server s) {
        List<TextChannel> blacklisted = new ArrayList<TextChannel>();
        List<GuildChannel> channels = g.getChannels();

        List<Long> spamID = s.getBlacklistedChannels();
        for (GuildChannel c : channels) {
            if (spamID == null)
                return null;
            for (Long id : spamID) {
                if (c.getIdLong() == id)
                    blacklisted.add((TextChannel) c);
            }
        }
        return blacklisted;
    }

    public static List<String> loadLeaderboard(Guild g) {
        List<Player> players = AlloyUtil.loadAllPlayers(g);
        List<String> positions = new ArrayList<String>();

        Collections.sort(players);

        for (Player player : players) {
            if (positions.size() <= MAX_LB_LENGTH)
                positions.add(getLBRank(player));
        }

        return positions;
    }

    private static String getLBRank(Player player) {
        int xp = player.getXP();

        List<Rank> stock = AlloyUtil.loadAllGlobalRanks();

        int level = findLevel(stock, xp);
        String progress = findProgress(xp, level, stock);

        return "<@!" + player.getId() + ">\nlevel: `" + level + "`\nxp: `" + progress + "`\n";
    }

    private static int findLevel(List<Rank> stock, int xp) {
        for (Rank rank : stock) {
            if (rank.getTotalXP() > xp)
                return rank.getLevel() - 1;

        }
        return 40;
    }

    private static String findProgress(int xp, int level, List<Rank> stock) {
        int nextLevel = level < stock.size() ? level : stock.size() - 1;
        Rank rank = stock.get(level - 1);
        Rank nextRank = stock.get(nextLevel);
        int progress = xp - rank.getTotalXP();
        String progressCombined = "" + progress + "/" + nextRank.getXP();
        return progressCombined;

    }

    public static void seeRank(Member target, TextChannel channel, Sendable bot) 
    {
        Player p = AlloyUtil.loadPlayer(target);
        int xp = p.getXP();

        List<Rank> stock = AlloyUtil.loadAllGlobalRanks();
        int level = findLevel(stock, xp);
        String progress = findProgress(xp, level, stock);

        Template t = Templates.rank(target, level, progress);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("RankHandler");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

    public static void announceRankUp(Rank rank, Member m, boolean addLevel, Sendable bot, TextChannel channel) {
        Guild g = m.getGuild();
        Server s = AlloyUtil.loadServer(g);

        List<RankUp> rankups = s.getRankups();

        for (RankUp rankup : rankups) {
            if (rank.getLevel() == rankup.getLevel()) {
                String message = replace(m, rankup);
                SendableMessage sm = new SendableMessage();
                sm.setChannel(channel);
                sm.setFrom("RankHandler");
                sm.setMessage(message);
                bot.send(sm);
                if (rankup.getId() != 0l && addLevel)
                    addRank(m, rankup.getId());
                return;
            }
        }
        String message = loadDefaultMessage(m, rank);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("RankHandler");
        sm.setMessage(message);
        bot.send(sm);
    }

    private static String loadDefaultMessage(Member m, Rank rank) {
        String message = FileReader.read(AlloyUtil.GLOBAL_RANKUP_TEXT_PATH)[0];
        message = replace(m, message, rank);
        return message;

    }

    private static String replace(Member m, String message, Rank rank) {
        User author = m.getUser();
        message = message.replace(RankUp.USER, author.getAsTag());
        message = message.replace(RankUp.USER_PING, author.getAsMention());
        message = message.replace(RankUp.LEVEL, "`" + rank.getLevel() + "`");

        return message;
    }

    private static String replace(Member m, RankUp rankup) {
        User author = m.getUser();
        String message = rankup.getMessage();
        message = message.replace(RankUp.USER, author.getAsTag());
        message = message.replace(RankUp.USER_PING, author.getAsMention());
        message = message.replace(RankUp.LEVEL, "`" + rankup.getLevel() + "`");
        if (rankup.getId() != 0l)
            message = message.replace(RankUp.ROLE, "<@&" + rankup.getId() + ">");

        return message;
    }

    private static void addRank(Member m, long id) {
        Guild g = m.getGuild();
        Role r = g.getRoleById(id);
        g.addRoleToMember(m, r).queue();
    }

    public static Member findUser(String user, Sendable bot, TextChannel channel) {
        Guild g = channel.getGuild();
        Member member = DisUtil.findMember(g, user);
        return member;
    }

    public static void setXP(Member m, int xp) {
        Guild g = m.getGuild();
        Player p = AlloyUtil.loadPlayer(g, m);
        p.setXP(xp);
    }

}
