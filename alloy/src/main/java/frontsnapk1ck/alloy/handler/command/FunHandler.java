package frontsnapk1ck.alloy.handler.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import frontsnapk1ck.alloy.gameobjects.RankUp;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.gameobjects.player.Player;
import frontsnapk1ck.alloy.gameobjects.player.Rank;
import frontsnapk1ck.alloy.handler.util.SpamContainer;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.main.intefs.Queueable;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.intefs.handler.CooldownHandler;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.discord.DisUtil;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import frontsnapk1ck.alloy.utility.job.jobs.AddUserXPCooldownJob;
import frontsnapk1ck.alloy.utility.job.jobs.RmUserXPCooldownJob;
import frontsnapk1ck.disterface.util.template.Template;
import frontsnapk1ck.io.FileReader;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;
import frontsnapk1ck.utility.event.Job;

public class FunHandler {

    public static final int MAX_LB_LENGTH = 10;
    
    private static  List<SpamContainer> spams;

    static {
        configure();
    }

    private static void configure() 
    {
        spams = new ArrayList<SpamContainer>();
    }

    public static SpamContainer makeRunnable(Guild guild, String[] args, User author, Sendable sendable, Queueable queue) 
    {
        Long num = getRandomNumber();
        Server s = AlloyUtil.loadServer(guild);
        Long channelID = s.getSpamChannel();
        TextChannel c = DisUtil.findChannel(guild, channelID);

        if (validCommand(args)) {
            int reps = getReps(guild, author, args);
            String message = buildMessage(args);

            SpamContainer container = new SpamContainer(reps, message, c, num , sendable , queue);
            container.setId(num);
            spams.add(container);

            return container;
        }
        return null;
    }

    public static boolean validCommand(String[] args) {
        boolean valid = false;
        valid = args.length >= 2;
        if (!valid)
            return false;

        try {
            int num = Integer.parseInt(args[0]);
            if (num < 1)
                return false;
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    private static Long getRandomNumber() 
    {
        boolean valid = false;
        Long l = 0l;

        while (!valid) 
        {
            l = (long) (Math.random() * 100000000000l);
            if (spams.size() == 0)
                valid = true;
            for (SpamContainer r : spams)
                valid = (r.getID() == l) ? false : true;
        }
        return l;
    }

    private static String buildMessage(String[] args) {
        String message = "";
        for (int i = 1; i < args.length; i++)
            message += args[i] + " ";

        message = message.replace("@everyone", "@ everyone");
        return message;
    }

    private static int getReps(Guild g, User author, String[] args) {
        int num = Integer.parseInt(args[0]);

        boolean five = !DisPermUtil.checkPermission(g.getMember(author), DisPerm.ADMINISTRATOR)
                && author.getIdLong() != 312743142828933130l;

        boolean sixty = !DisPermUtil.checkPermission(g.getMember(author), DisPerm.ADMINISTRATOR)
                && author.getIdLong() != 312743142828933130l;
        if (five)
            num = num > 5 ? 5 : num;
        else if (sixty)
            num = num > 60 ? 60 : num;
        return num;

    }

    public static boolean isStart(String[] args) {
        try {
            Integer.parseInt(args[0]);
            return args.length > 1;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isStop(String[] args) {
        try {
            Long.parseLong(args[1]);
            return args[0].equalsIgnoreCase("stop");
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean stopSpam(Long id) 
    {
        SpamContainer toRm = null;
        for (SpamContainer r : spams) 
        {
            if (r.getID().equals(id))
                toRm = r;
        }

        if (toRm != null) 
        {
            toRm.stop();
            spams.remove(toRm);
        }

        return toRm != null;
    }

    public static boolean isWhitelisted(Member m) {
        List<Long> whitelisted = AlloyUtil.getWhitelisted();
        for (Long id : whitelisted) {
            if (id == m.getIdLong())
                return true;
        }
        return false;
    }

    public static void sayRaw(TextChannel channel, Sendable bot, Message msg) 
    {
        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>() 
        {
            @Override
            public void accept(ErrorResponseException t) 
            {
                Alloy.LOGGER.warn("DeleteMessageJob", t.getMessage());
            }

            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE, consumer);

        
        if (msg.getAttachments().size() != 0)
            sendAttachments(msg,channel,bot);
        else 
            sendNormal(msg,channel,bot);
        msg.delete().queue(null , handler);
    }

    private static void sendAttachments(Message msg, TextChannel channel, Sendable bot) 
    {
        List<Attachment> attachments = msg.getAttachments();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(FunHandler.class);

        for (Attachment attachment : attachments) 
        {
            String path = AlloyUtil.TMP_FOLDER;
            path += msg.getChannel().getId() + "-" + msg.getId();
            path += "." + attachment.getFileExtension();
            File f = new File(path);

            try 
            {
                File down = attachment.downloadToFile(f).get();
                sm.addFile(down);
            }
            catch (InterruptedException | ExecutionException e1) 
            {
                Alloy.LOGGER.error("JDAEvents", e1);
            }
        }

        try {
            String out = msg.getContentRaw().toString().substring(5);
            sm.setMessage(out);    
        } catch (Exception e) 
        {
            sm.setMessage(" ");
        }
        bot.send(sm);
    }

    private static void sendNormal(Message msg, TextChannel channel, Sendable bot) 
    {
        if (msg.getContentRaw().length() < 5 )
            return;

        String out = msg.getContentRaw().toString().substring(5);

        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(FunHandler.class);
        sm.setMessage(out);
        bot.send(sm);
    }

    public static void sayAdmin(TextChannel channel, Sendable bot, Message msg) 
    {

        if (msg.getContentRaw().length() < 5 )
            return;
        
        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>() 
        {
            @Override
            public void accept(ErrorResponseException t) 
            {
                Alloy.LOGGER.warn("DeleteMessageJob", t.getMessage());
            }

            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE, consumer);

        String out = msg.getContentRaw().toString().substring(5);
        Template t = Templates.sayAdmin(out, msg);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(FunHandler.class);
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        msg.delete().queue(null, handler);
    }

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

    public static List<String> loadLeaderboard(Guild g) 
    {
        List<Player> players = AlloyUtil.loadAllPlayers(g);
        List<String> positions = new ArrayList<String>();

        Comparator<Player> comparator = new Comparator<Player>()
        {
            public int compare(Player o1, Player o2) 
            {
                if (o1.getXP() > o2.getXP())
                    return -1;
                else if (o1.getXP() < o2.getXP())
                    return 1;
                return 0;  
            };
        };
 

        Collections.sort(players , comparator);

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
        sm.setFrom(FunHandler.class);
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
                sm.setFrom(FunHandler.class);
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
        sm.setFrom(FunHandler.class);
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
    
    public static String[] getDeadChats() 
	{
		String[] out = FileReader.read(AlloyUtil.CHATS_FILE);
		return out;
	}

	public static String[] getRankChats() 
	{
		String[] out = FileReader.read(AlloyUtil.RANKS_FILE);
		return out;
	}
	
	public static String[] getWarnChats() 
	{
		String[] out = FileReader.read(AlloyUtil.WARNS_FILE);
		return out;
	}

}
