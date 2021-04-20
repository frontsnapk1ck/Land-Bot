package frontsnapk1ck.alloy.handler.command;

import java.util.List;

import frontsnapk1ck.alloy.gameobjects.RankUp;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.gameobjects.player.Building;
import frontsnapk1ck.alloy.gameobjects.player.Rank;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.intefs.handler.CooldownHandler;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.discord.DisUtil;
import frontsnapk1ck.alloy.utility.settings.RankUpSettings;
import frontsnapk1ck.io.FileReader;
import frontsnapk1ck.utility.StringUtil;
import frontsnapk1ck.utility.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class ConfigHandler {

    public static void viewBuildings(Guild g, TextChannel channel, Sendable bot) 
    {
        EmbedBuilder eb = new EmbedBuilder();
        List<Building> buildings = AlloyUtil.reloadBuildings(g);

        eb.setTitle("All available buildings");
        embedBuildingList(eb, buildings);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(ConfigHandler.class);
        sm.setMessage(eb.build());
        bot.send(sm);
    }

    private static void embedBuildingList(EmbedBuilder eb, List<Building> buildings) {
        String names = "";
        String costs = "";
        String gener = "";

        for (Building b : buildings) {
            names += "" + b.getName() + "\n";
            costs += "" + b.getCost() + "\n";
            gener += "" + b.getGeneration() + "\n";

        }

        eb.addField("Name", names, true);
        eb.addField("Cost", costs, true);
        eb.addField("Generation", gener, true);
    }

    public static void viewWork(Guild g, TextChannel channel, Sendable bot) {
        EmbedBuilder eb = new EmbedBuilder();
        String path = AlloyUtil.getGuildPath(g) + AlloyUtil.SETTINGS_FOLDER + AlloyUtil.SUB
                + AlloyUtil.WORK_OPTIONS_FILE;
        String[] wO = FileReader.read(path);

        eb.setTitle("All available work options");
        embedWorkArray(eb, wO);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(ConfigHandler.class);
        sm.setMessage(eb.build());
        bot.send(sm);

    }

    private static void embedWorkArray(EmbedBuilder eb, String[] options) {
        String num = "";
        String name = "";

        int i = 1;
        for (String string : options) {
            num += i + "\n";
            name += string + "\n";
            i++;
        }

        eb.addField("#", num, true);
        eb.addField("name", name, true);
    }

    public static void viewStartingBalance(AlloyInputData data) 
    {
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Guild g = data.getGuild();

        Server s = AlloyUtil.loadServer(g);

        AlloyTemplate t = Templates.viewStartingBalance(s.getStartingBalance());
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(ConfigHandler.class);
        sm.setMessage(t.getEmbed());
        bot.send(sm);  
	}

    public static void changeStartingBalance(AlloyInputData data, String balS) 
    {
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Guild g = data.getGuild();

        Server s = AlloyUtil.loadServer(g);

        try 
        {
            int bal = Util.parseInt(balS);
            s.changeStartingBalance(bal);
        }
        catch (NumberFormatException e)
        {
            AlloyTemplate t = Templates.invalidNumberFormat(balS);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(ConfigHandler.class);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        
        
	}

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
            AlloyTemplate t = Templates.invalidNumberFormat(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(ConfigHandler.class);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!vRole) {
            AlloyTemplate t = Templates.invalidRole(args[2]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(ConfigHandler.class);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String role = DisUtil.trimMention(args[2]);
        long id = Long.parseLong(role);
        int level = Integer.parseInt(args[1]);

        if (isDuplicateRankup(level, s)) {
            AlloyTemplate t = Templates.duplicateRankup(level);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(ConfigHandler.class);
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
            AlloyTemplate t = Templates.invalidNumberFormat(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(ConfigHandler.class);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        int level = Integer.parseInt(args[1]);

        if (isDuplicateRankup(level, s)) {
            AlloyTemplate t = Templates.duplicateRankup(level);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(ConfigHandler.class);
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
        AlloyTemplate t = Templates.rankupAddSuccess(ru);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(ConfigHandler.class);
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
            AlloyTemplate t = Templates.rankupRemoveSuccess(toRm);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(ConfigHandler.class);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        else {
            AlloyTemplate t = Templates.levelNotFound("" + level);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(ConfigHandler.class);
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

    public static void setXpCooldown(Guild g, int newTime) {
        Server s = AlloyUtil.loadServer(g);
        s.changeXPCooldown(newTime);
    }

    public static void setCooldown(Guild g, int newTime) {
        Server s = AlloyUtil.loadServer(g);
        s.changeCooldown(newTime);
    }

    public static void showXPCooldown(AlloyInputData data) {
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Guild g = data.getGuild();

        Server s = AlloyUtil.loadServer(g);
        int cooldown = s.getXPCooldown();

        AlloyTemplate t = Templates.showXPCooldown(cooldown);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(CooldownHandler.class);
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

    public static void showCooldown(AlloyInputData data) {
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Guild g = data.getGuild();

        Server s = AlloyUtil.loadServer(g);
        int cooldown = s.getWorkCooldown();

        AlloyTemplate t = Templates.showCooldown(cooldown);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(CooldownHandler.class);
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

    public static void view(Guild g, TextChannel channel, Sendable bot) 
    {
        Server s = AlloyUtil.loadServer(g);
        List<Long> blacklisted = s.getBlacklistedChannels();
        AlloyTemplate t;
        if ( blacklisted == null || blacklisted.size() == 0 )
            t = Templates.noBlacklistedChannels();
        else
            t = Templates.listBlackListedChannels(blacklisted);
        
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom(ConfigHandler.class);
        bot.send( sm );
	}

    public static boolean isBlacklisted(Guild g, String channel) 
    {
        Server s = AlloyUtil.loadServer(g);
        channel = channel.replace("<#", "");
        channel = channel.replace(">", "");

        return s.getBlacklistedChannels().contains(Long.parseLong(channel));
	}

    public static void add(Guild g, String channel )
    {
        Server s = AlloyUtil.loadServer(g);
        channel = channel.replace("<#", "");
        channel = channel.replace(">", "");

        s.addBlacklistedChanel(Long.parseLong(channel));

	}

    public static boolean remove(Guild g, String channel) 
    {
        Server s = AlloyUtil.loadServer(g);
        channel = channel.replace("<#", "");
        channel = channel.replace(">", "");

        return s.removeBlackListedChannel(Long.parseLong(channel));
	}
    
}