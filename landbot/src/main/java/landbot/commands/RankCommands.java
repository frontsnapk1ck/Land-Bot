package landbot.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import landbot.builder.loaders.PlayerLoaderText;
import landbot.builder.loaders.RankLoaderText;
import landbot.builder.loaders.ServerLoaderText;
import landbot.gameobjects.RankUp;
import landbot.gameobjects.Server;
import landbot.gameobjects.player.Player;
import landbot.gameobjects.player.Rank;
import landbot.io.FileReader;
import landbot.utility.PlayerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class RankCommands extends PlayerCommand {

    List<Player> cooldownUsers;

    public RankCommands() 
    {
        super(RankCommands.class.getName());
        cooldownUsers = new ArrayList<Player>();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);
        if (e.getAuthor().isBot())
            return;

        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        this.addXP(e, s);

        String[] args = e.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(s.getPrefix() + "rank"))
            seeRank(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "lb"))
            leaderboard(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "help"))
            help(e);

    }

    private void leaderboard(GuildMessageReceivedEvent e) {
        String path = getGuildPath(e.getGuild());
        path += "\\users";
        PlayerLoaderText plt = new PlayerLoaderText();
        List<Player> players = plt.loadALl(path);
        List<String> positions = new ArrayList<String>();
        Collections.sort(players);

        for (Player player : players)
            positions.add(getLBRank(player));

        String out = "";
        int maxShown = 10;
        int maxIndex = maxShown > positions.size() ? positions.size() : maxShown;
        for (int i = 0; i < maxIndex; i++)
            out += "**" + (i + 1) + "**\t" + positions.get(i) + "\n";

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Leaderboard");
        eb.setDescription(out);

        e.getChannel().sendMessage(eb.build()).queue();
    }

    private String getLBRank(Player player) {
        int xp = player.getXP();

        RankLoaderText rlt = new RankLoaderText();
        List<Rank> stock = rlt.loadALl("landbot\\res\\globals\\defualt\\rank.txt");

        int level = findLevel(stock, xp);
        String progress = findProgess(xp, level, stock);

        return "<@!" + player.getId() + ">\nlevel: `" + level + "`\nxp: `" + progress + "`\n";
    }

    private void seeRank(GuildMessageReceivedEvent e, String[] args) {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        String path = "";
        long id = 0l;

        if (args.length == 1) 
        {
            path = getUserPath(e);
            id = e.getAuthor().getIdLong();
        } 
        
        else if (validUser(args[1])) 
        {
            String user = args[1];
            user = user.replace("<@!", "");
            user = user.replace("<@", "");
            user = user.replace(">", "");

            path = getUserPath(e.getGuild(), args[1]);
            id = Long.parseLong(user);

        } 
        else 
        {
            path = getUserPath(e);
            id = e.getAuthor().getIdLong();
        }

        checkUserFile(id, s);

        PlayerLoaderText plt = new PlayerLoaderText();
        RankLoaderText rlt = new RankLoaderText();
        Player p = plt.load(path);

        int xp = p.getXP();

        List<Rank> stock = rlt.loadALl("landbot\\res\\globals\\defualt\\rank.txt");

        int level = findLevel(stock, xp);
        String progress = findProgess(xp, level, stock);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Rank");
        String member = "<@!" + id + ">";
        eb.setDescription(member + "\nlevel: `" + level + "`\nxp: `" + progress + "`");
        e.getChannel().sendMessage(eb.build()).queue();

    }

    private String findProgess(int xp, int level, List<Rank> stock) {
        int nextLevel = level < stock.size() ? level : stock.size();
        Rank rank = stock.get(level - 1);
        Rank nextRank = stock.get(nextLevel);
        int progress = xp - rank.getTotalXP();
        String progressCombined = "" + progress + "/" + nextRank.getXP();
        return progressCombined;

    }

    private int findLevel(List<Rank> stock, int xp) {
        for (Rank rank : stock) {
            if (rank.getTotalXP() > xp)
                return rank.getLevel() - 1;

        }
        return 40;
    }

    private String getUserPath(Guild guild, String user) {
        user = user.replace("<@!", "");
        user = user.replace("<@", "");
        user = user.replace(">", "");

        long id = Long.parseLong(user);

        return getUserPath(guild, id);

    }

    private String getUserPath(Guild g, long id) {
        String path = getGuildPath(g);
        path += "\\users\\";
        path += "" + id;
        return path;
    }

    private String getUserPath(GuildMessageReceivedEvent e) {
        long id = e.getAuthor().getIdLong();
        Guild g = e.getGuild();

        return getUserPath(g, id);
    }

    private void addXP(GuildMessageReceivedEvent e, Server s) {
        List<TextChannel> blacklisted = getBlacklistedChannels(e, s);
        if (channelIn(blacklisted, e.getChannel()))
            return;

        long id = e.getAuthor().getIdLong();
        if (userInCooldown(id))
            return;

        PlayerLoaderText plt = new PlayerLoaderText();
        String pPath = getGuildPath(e.getGuild()) + "\\users\\" + id;
        Player p = plt.load(pPath);
        p.addXP(1);
        checkLevelUp(e, p);

        Runnable r = makeRunnable(p, s);
        Thread t = new Thread(r, "XP cooldown " + e.getAuthor().getAsTag());

        t.setDaemon(true);
        t.start();
    }

    private void checkLevelUp(GuildMessageReceivedEvent e, Player p) {
        RankLoaderText rlt = new RankLoaderText();
        List<Rank> stock = rlt.loadALl("landbot\\res\\globals\\defualt\\rank.txt");

        for (Rank rank : stock) {
            int playerXP = p.getXP();
            int rankXP = rank.getTotalXP();
            if (playerXP == rankXP)
                announceLevelUp(e, rank);
        }
    }

    private void announceLevelUp(GuildMessageReceivedEvent e, Rank rank) 
    {
        announceLevelUp(e, rank , true);
    }

    public static void announceLevelUp(GuildMessageReceivedEvent e, Rank rank, boolean addLevel) 
    {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        List<RankUp> rankups = s.getRankups();

        for (RankUp rankup : rankups) {
            if (rank.getLevel() == rankup.getLevel()) 
            {
                announceLevelUp(e, replace(e, rankup.getMessage(), rankup));
                if (rankup.getId() != 0l && addLevel)
                    addRank(e, rankup.getId());
                return;
            }
        }

        String message = "congratulations on reacting level `" + rank.getLevel() + "` " + e.getAuthor().getAsMention();
        announceLevelUp(e, message);
    }

    private static void addRank(GuildMessageReceivedEvent e, long id) 
    {
        Role role = e.getGuild().getRoleById(id);
        long userID = e.getAuthor().getIdLong();
        e.getGuild().addRoleToMember( userID , role).queue();
    }

    private static void announceLevelUp(GuildMessageReceivedEvent e, String message) 
    {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Rank up");
        eb.setDescription(message);
        eb.setFooter(e.getAuthor().getAsTag(), e.getAuthor().getAvatarUrl());

        e.getChannel().sendMessage(e.getAuthor().getAsMention()).queue();
        e.getChannel().sendMessage(eb.build()).queue();
    }

    private static String replace(GuildMessageReceivedEvent e, String message, RankUp rankup) 
    {
        message = message.replace(RankUp.USER, e.getAuthor().getAsTag());
        message = message.replace(RankUp.USER_PING, e.getAuthor().getAsMention());
        message = message.replace(RankUp.LEVEL, "`" + rankup.getLevel() + "`");
        if (rankup.getId() != 0l)
            message = message.replace(RankUp.ROLE, "<@&" + rankup.getId() + ">");

        return message;
    }


    private Runnable makeRunnable(Player p, Server s) 
    {
        List<Player> cooldownUsers2 = cooldownUsers;
        Runnable r = new Runnable() 
        {
            @Override
            public void run() {
                cooldownUsers2.add(p);
                coolDown(s.getXPCooldown());
                cooldownUsers2.remove(p);

            }

            private void coolDown(int xpCooldown) 
            {
                try 
                {
                    Thread.sleep(xpCooldown * 1000);
                } 
                catch (InterruptedException e) 
                {
                    e.printStackTrace();
                }
            }
        };
        return r;
    }

    private boolean userInCooldown(long id) 
    {
        for (Player player : cooldownUsers) 
        {
            if (player.getId() == id)    
                return true;
        }
        return false;
    }

    private boolean channelIn(List<TextChannel> blacklisted, TextChannel channel) 
    {
        if (blacklisted == null)
            return false;
            
        for (TextChannel c : blacklisted) 
        {
            if (c == channel)
                return true;
        }
        return false;
    }

    @Override
    protected void help(GuildMessageReceivedEvent e) 
    {
        PrivateChannel c = e.getAuthor().openPrivateChannel().complete();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Rank Commands");

        String message = loadHelpMessage();
        eb.setDescription(message);

        c.sendMessage(eb.build()).queue();
    }

    private String loadHelpMessage() 
    {
        String out = "";
        String[] message = FileReader.read("landbot\\res\\globals\\help\\rankHelp.msg");
        for (String string : message)
            out += string + "\n";
        return out;
    }

    private List<TextChannel> getBlacklistedChannels(GuildMessageReceivedEvent e, Server s) 
    {
        List<TextChannel> blacklisted = new ArrayList<TextChannel>();
        List<GuildChannel> channels = e.getGuild().getChannels();

        List<Long> spamID = s.getBlacklistedChannels();
        for (GuildChannel c : channels) 
        {
            if (spamID == null)
                return null;
            for (Long id : spamID) 
            {
                if (c.getId().contentEquals("" + id))
                    blacklisted.add( (TextChannel)c );
            }
        }
        return blacklisted;
    }
    
}
