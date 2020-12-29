package alloy.commands;

import java.util.Collections;
import java.util.List;

import alloy.builder.loaders.BuildingLoaderText;
import alloy.builder.loaders.PlayerLoaderText;
import alloy.builder.loaders.RankLoaderText;
import alloy.builder.loaders.ServerLoaderText;
import alloy.gameobjects.RankUp;
import alloy.gameobjects.Server;
import alloy.gameobjects.player.Building;
import alloy.gameobjects.player.Player;
import alloy.gameobjects.player.Rank;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.PlayerCommand;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.settings.BuildingSettings;
import alloy.utility.settings.RankUpSettings;
import io.FileReader;
import io.Saver;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class AdminCommands extends PlayerCommand {

    private String[] building;
    private boolean buildingB;
    private User buildingConstrucort;
    private boolean removeBuilding;
    private boolean removeWorkOption;
    private List<Guild> guilds;

    public AdminCommands() 
    {
        super(AdminCommands.class.getName());
        Runnable day = makeDayRunnable();

        Thread t = new Thread(day, "Day Timer");
        t.setDaemon(true);
        t.start();
    }

    private Runnable makeDayRunnable() 
    {
        Runnable day = new Runnable() 
        {
            @Override
            public void run() 
            {
                while (true) 
                {
                    try 
                    {
                        Thread.sleep (
                            24 * // hours
                            60 * // mins
                            60 * // secs
                            1000 // milis
                        );
                        day();
                        logger.info("The day has advanced for all guilds");
                    }

                    catch (InterruptedException e) 
                    {
                        logger.error("Day Thread caught am error while waiting for the day to advace");
                        e.printStackTrace();
                    }
                }
            }
        };

        return day;
    }

    private void day() 
    {
        for (Guild g : guilds) 
        {
            String path = getGuildPath(g) + "\\users";
            PlayerLoaderText plt = new PlayerLoaderText();
            AlloyUtil.checkFileSystem(g);

            //TEMP
            
            //TEMP
            List<Player> players = plt.loadALl(path);
            for (Player p : players)
                p.day();
        }

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) 
    {
        super.onGuildMessageReceived(e);
        if (!validSender(e))
            return;

        String[] args = e.getMessage().getContentRaw().split(" ");
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        if (args[0].equalsIgnoreCase(s.getPrefix() + "prefix"))
            prefixCommand(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "starting-balance"))
            startingBalanceCommand(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "cooldown"))
            cooldown(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "reset-buildings"))
            resetBuildings(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "add-building"))
            addBuilding(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "rm-building"))
            removeBuilding(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "reset-work"))
            resetWorkOptions(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "add-work"))
            addWorkOption(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "rm-work"))
            removeWorkOption(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "view-work"))
            viewWorkOptions(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "day"))
            day(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "buy-roles"))
            buyRoles(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "admin-bypass"))
            adminCooldown(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "spam-channel"))
            setSpam(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "help"))
            help(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "set-xp"))
            setXP(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "xp-blacklist-add"))
            blacklistAdd(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "xp-blacklist-rm"))
            blacklistRemove(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "xp-cooldown"))
            xpCooldown(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "rankup-add"))
            addRankup(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "rankup-rm"))
            rmRankup(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "rankup-view"))
            viewRankup(e);
        
        else if (args[0].equalsIgnoreCase(s.getPrefix() + "rankup-test"))
            rankupTest(e , args);

        else if (args[0].equalsIgnoreCase("<@!762825892006854676>"))
            pingedMe(e , args);

        else if ((this.removeBuilding || this.buildingB || this.removeWorkOption)
                && e.getAuthor().equals(this.buildingConstrucort) && args[0].contains("cancel"))
            cancelProgress(e);

        else if (this.removeBuilding && e.getAuthor().equals(this.buildingConstrucort))
            selectRemoveBuilding(e);

        else if (this.buildingB && e.getAuthor().equals(this.buildingConstrucort))
            buildBuilding(args[0], e);

        else if (this.removeWorkOption && e.getAuthor().equals(this.buildingConstrucort))
            selectWorkOptionRemove(e);

        else if (e.getAuthor() == this.buildingConstrucort)
            cancelProgress(e);

    }

    private void pingedMe(GuildMessageReceivedEvent e, String[] args) 
    {
        logger.debug("pinged in " + e.getGuild().getName() + "\t" + e.getGuild().getId() );
        if (args.length != 1)
            return;
        viewPrefix(e);
    }

    private void rankupTest(GuildMessageReceivedEvent e, String[] args) 
    {
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;
        cancelProgress(e);

        ServerLoaderText slt = new ServerLoaderText();
        RankLoaderText rlt = new RankLoaderText();

        Server s = slt.load(getGuildPath(e.getGuild()));


        if (!validInt(args[1]))
        {
            error(e.getChannel(), "please enter a valid integer");
            return;
        }
        int level = Integer.parseInt(args[1]);
        if (!levelInRankups( s.getRankups() , level))
        {
            error(e.getChannel() , "please enter a number that corrosponds to a level with a custom anouncement");
        }

        Rank r = rlt.loadALl(AlloyUtil.ALLOY_PATH + "res\\globals\\defualt\\rank.txt").get(level-1);
        RankCommands.announceLevelUp(e, r , false);
    }

    private boolean levelInRankups(List<RankUp> list, int level) 
    {
        boolean in = false;
        for (RankUp rankUp : list) 
        {
            if (rankUp.getLevel() == level)
                in = true;    
        }
        return in;
    }

    private void viewRankup(GuildMessageReceivedEvent e) {
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;
        cancelProgress(e);

        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        List<RankUp> rankups = s.getRankups();
        String out = "";
        for (RankUp rankUp : rankups)
            out += rankUp.toString() + "\n";

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Rank Up Messages");
        if (out == "")
            out = "nothing has been added yet";

        eb.setDescription(out);

        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void rmRankup(GuildMessageReceivedEvent e, String[] args) 
    {
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;
        cancelProgress(e);

        if (args.length == 1)
            viewRankup(e);
        else
            rmRankupFromGuild(e, args);
    }

    private void rmRankupFromGuild(GuildMessageReceivedEvent e, String[] args) 
    {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        if (!validInt(args[1])) {
            error(e.getChannel(), "invalid number for level");
            return;
        }

        int level = Integer.parseInt(args[1]);
        List<RankUp> rankups = s.getRankups();
        RankUp toRm = null;

        for (RankUp rankUp : rankups) {
            if (rankUp.getLevel() == level)
                toRm = rankUp;
        }
        if (toRm != null)
            s.removeRankUp(toRm);
        viewRankup(e);
    }

    private void addRankup(GuildMessageReceivedEvent e, String[] args) 
    {
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;
        cancelProgress(e);

        if (args.length == 1)
            viewRankup(e);
        else
            addRankuptoGuildWithRole(e, args);
    }

    private void addRankuptoGuildWithRole(GuildMessageReceivedEvent e, String[] args) 
    {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        boolean numB = validInt(args[1]);
        boolean roleB = validRole(args[2]);

        if (!numB) 
        {
            error(e.getChannel(), "invalid number for level");
            return;
        }

        if (!roleB) 
        {
            addRankuptoGuild(e, args);
            return;
        }

        String role = args[2];
        role = role.replace("<@&", "");
        role = role.replace(">", "");

        int level = Integer.parseInt(args[1]);
        long id = Long.parseLong(role);

        if (rankUpDuplicate(level, s)) {
            error(e.getChannel(), "Duplicate Level");
            return;
        }

        String out = "";
        for (int i = 3; i < args.length; i++)
            out += args[i] + " ";

        RankUpSettings settings = new RankUpSettings();
        settings.setId(id)
                .setLevel(level)
                .setMessage(out);

        RankUp ru = new RankUp(settings);

        s.addRankUp(ru);

        viewRankup(e);
    }

    private void addRankuptoGuild(GuildMessageReceivedEvent e, String[] args) 
    {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        if (!validInt(args[1]))
            return;

        int level = Integer.parseInt(args[1]);

        if (rankUpDuplicate(level, s)) {
            error(e.getChannel(), "Duplicate Level");
            return;
        }

        String out = "";
        for (int i = 2; i < args.length; i++)
            out += args[i] + " ";

            RankUpSettings settings = new RankUpSettings();
            settings.setLevel(level)
                    .setMessage(out);
    
            RankUp ru = new RankUp(settings);

        s.addRankUp(ru);
        viewRankup(e);

    }

    private void error(TextChannel c, String s) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Something Went Wrong");
        eb.setDescription(s);

        c.sendMessage(eb.build()).queue();
    }

    private boolean rankUpDuplicate(int level, Server s) 
    {
        boolean b = false;
        for (RankUp ru : s.getRankups()) 
        {
            if (ru.getLevel() == level)
                b = true;
        }

        return b;
    }

    private void xpCooldown(GuildMessageReceivedEvent e, String[] args) {
        if (args.length == 1)
            showXPCooldown(e);
        else
            changeXPCooldown(e, args);
    }

    private void changeXPCooldown(GuildMessageReceivedEvent e, String[] args) {
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;
        cancelProgress(e);

        if (args.length == 1 || !validInt(args[1]))
            return;

        int newCooldown = Integer.parseInt(args[1]);
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        s.changeXPCooldown(newCooldown);

        showXPCooldown(e);

    }

    private void showXPCooldown(GuildMessageReceivedEvent e) {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("XP Cooldown Time");
        eb.setDescription("the cooldown time for xp is currently `" + s.getXPCooldown() + '`');
        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void blacklistAdd(GuildMessageReceivedEvent e, String[] args) 
    {
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;
        cancelProgress(e);

        if (args.length == 1)
            showBlacklisted(e);
        else
            addXPBlacklist(e, args);
    }

    private void addXPBlacklist(GuildMessageReceivedEvent e, String[] args) {
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;
        cancelProgress(e);

        if (args.length < 2) {
            error(e.getChannel(), "please suply a channel to blacklist");
            return;
        }

        if (!validChannel(args[1])) {
            error(e.getChannel(), "please suply a valid channel to blacklist");
            return;
        }

        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        String channel = args[1].replace("<#", "");
        channel = channel.replace(">", "");
        long id = Long.parseLong(channel);

        s.addBlacklistedChanel(id);

        showBlacklisted(e);
    }

    private boolean validChannel(String channel) {
        channel = channel.replace("<#", "");
        channel = channel.replace(">", "");

        try {
            Long.parseLong(channel);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showBlacklisted(GuildMessageReceivedEvent e) 
    {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        if (s.getBlacklistedChannels() == null || s.getBlacklistedChannels().size() == 0) 
        {
            displayEmptyBlackList(e.getChannel());
            return;
        }

        String out = "";
        for (long l : s.getBlacklistedChannels())
            out += "<#" + l + ">\n";

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Blacklisted Channels");
        eb.setDescription(out);

        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void displayEmptyBlackList(TextChannel channel) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Blacklisted Channels");
        eb.setDescription("there are currently no channels blacklisted for users gaining XP");

        channel.sendMessage(eb.build()).queue();
    }

    private void blacklistRemove(GuildMessageReceivedEvent e, String[] args) {
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;
        cancelProgress(e);

        if (args.length == 1)
            showBlacklisted(e);
        else
            rmXPBlacklist(e, args);
    }

    private void rmXPBlacklist(GuildMessageReceivedEvent e, String[] args) {
        if (args.length < 2) {
            error(e.getChannel(), "please suply a channel to blacklist");
            return;
        }

        if (!validChannel(args[1])) {
            error(e.getChannel(), "please suply a valid channel to blacklist");
            return;
        }

        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        String channel = args[1].replace("<#", "");
        channel = channel.replace(">", "");
        long id = Long.parseLong(channel);

        s.removeBlackListedChannel(id);

        showBlacklisted(e);
    }

    private void setXP(GuildMessageReceivedEvent e, String[] args) 
    {
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;
        cancelProgress(e);

        if (args.length < 3) {
            error(e.getChannel(), "please enter all parameters");
            return;
        }
        if (!validInt(args[1])) {
            error(e.getChannel(), "please suply a valid integer");
            return;
        }

        if (!validUser(args[2])) {
            error(e.getChannel(), "please suply a valid user");
            return;
        }

        PlayerLoaderText plt = new PlayerLoaderText();
        String path = getUserPath(e, args[2]);
        Player p = plt.load(path);

        int xp = Integer.parseInt(args[1]);

        p.setXP(xp);

    }

    private boolean validInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validRole(String string) 
    {
        string = string.replace("<@&", "");
        string = string.replace(">", "");
        try {
            Long.parseLong(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void setSpam(GuildMessageReceivedEvent e, String[] args) {
        cancelProgress(e);
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;

        if (args.length == 1) {
            e.getChannel().sendTyping().queue();
            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle("Spam Channel");
            eb.addField(new Field("The Spam Channel is Currently", "<#" + s.getSpamChannel() + ">", true));
            e.getChannel().sendMessage(eb.build()).queue();
        }

        else {
            String spam = args[1];
            spam = spam.replace("<#", "");
            spam = spam.replace(">", "");
            boolean valid = false;
            List<GuildChannel> channels = e.getGuild().getChannels();
            for (GuildChannel c : channels) {
                if (c.getId().equals(spam))
                    valid = true;
            }

            if (valid) {
                s.changeSpamChannel(Long.parseLong(spam));
                EmbedBuilder eb = new EmbedBuilder();

                eb.setTitle("Spam Channel");
                eb.addField(new Field("The Spam Channel is now", "<#" + s.getSpamChannel() + ">", true));
                e.getChannel().sendMessage(eb.build()).queue();
            } else
                e.getChannel().sendMessage("Please enter a valid channel").queue();
            ;
        }
    }

    private void adminCooldown(GuildMessageReceivedEvent e, String[] args) {
        cancelProgress(e);
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;

        if (args.length == 1) {
            e.getChannel().sendTyping().queue();
            EmbedBuilder eb = new EmbedBuilder();

            String state = s.getAdminCooldownBypass() ? "on" : "off";

            eb.setTitle("Admin Cooldown Bypass");
            eb.addField(new Field("The Admin Bypass Cooldown is Currently", "`" + state + "`", true));
            e.getChannel().sendMessage(eb.build()).queue();
        }

        else {
            boolean valid = false;
            if (!valid) {
                try {
                    boolean b = Boolean.parseBoolean(args[1]);
                    valid = true;
                    s.changeAdminCooldownBypass(b);
                }

                catch (NumberFormatException err) {
                    e.getChannel().sendMessage("Please enter `true` pr `false`");
                }
            }

            EmbedBuilder eb = new EmbedBuilder();

            String state = s.getAdminCooldownBypass() ? "on" : "off";

            eb.setTitle("Admin Cooldown Bypass");
            eb.addField(new Field("The Admin Bypass Cooldown is Currently", "`" + state + "`", true));
            e.getChannel().sendMessage(eb.build()).queue();
        }
    }

    private void selectWorkOptionRemove(GuildMessageReceivedEvent e) {
        try {
            int rm = Integer.parseInt(e.getMessage().getContentRaw());
            ServerLoaderText slt = new ServerLoaderText();
            Server s = slt.load(getGuildPath(e.getGuild()));
            String path = s.getPath() + "\\settings\\work.options";
            String[] options = FileReader.read(path);

            if (rm > 0 && rm <= options.length) {
                options[rm - 1] = null;
                String[] newOp = new String[options.length - 1];

                int i = 0;
                for (String st : options) {
                    if (st != null) {
                        newOp[i] = st;
                        i++;
                    }
                }
                Saver.saveOverwite(path, newOp);
            } else
                e.getChannel().sendMessage("Please enter a valid number");

            this.viewWorkOptions(e);
            this.removeWorkOption = false;

        } catch (NumberFormatException ex) {
            e.getChannel().sendMessage("Please enter and Integer").queue();
        }
    }

    private void removeWorkOption(GuildMessageReceivedEvent e) {
        cancelProgress(e);
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;

        this.buildingConstrucort = e.getAuthor();
        this.removeWorkOption = true;
        viewWorkOptions(e, false);

    }

    private void resetWorkOptions(GuildMessageReceivedEvent e) {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        cancelProgress(e);
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;

        String[] out = { "you work at mcdonalds", "you work at burger king", "you work at walmart" };

        String path = s.getPath() + "\\settings\\work.options";
        Saver.saveOverwite(path, out);
        viewWorkOptions(e);
    }

    private void viewWorkOptions(GuildMessageReceivedEvent e, boolean b) {
        if (b)
            cancelProgress(e);
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;

        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("All availible work options");

        String path = s.getPath() + "\\settings\\work.options";
        String[] wO = FileReader.read(path);

        String num = "";
        String name = "";

        int i = 1;
        for (String string : wO) {
            num += i + "\n";
            name += string + "\n";
            i++;
        }

        eb.addField(new Field("#", num, true));
        eb.addField(new Field("name", name, true));

        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void viewWorkOptions(GuildMessageReceivedEvent e) {
        viewWorkOptions(e, true);
    }

    private void addWorkOption(GuildMessageReceivedEvent e, String[] args) {
        cancelProgress(e);
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;

        if (args.length == 1)
            informOnWork(e);

        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        String out = "";
        for (int i = 1; i < args.length; i++)
            out += args[i] + " ";

        String path = s.getPath() + "\\settings\\work.options";
        Saver.saveAppend(path, out);
    }

    private void informOnWork(GuildMessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Add Work");
        eb.setDescription(
                "Use this command to add options to the random work messages in this server\n\nbe sure to put the option you want after the command (e.g. `!add-work you work at walmart`)");
        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void buyRoles(GuildMessageReceivedEvent e, String[] args) {
        cancelProgress(e);
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;

        if (args.length == 1) {
            e.getChannel().sendTyping().queue();
            EmbedBuilder eb = new EmbedBuilder();

            String state = s.getRoleAssignOnBuy() ? "on" : "off";

            eb.setTitle("Auto Assign Roles");
            eb.addField(new Field("The Auto Assign Roles is Currently", "`" + state + "`", true));
            e.getChannel().sendMessage(eb.build()).queue();
        }

        else {
            boolean valid = false;
            while (!valid) {
                try {
                    boolean b = Boolean.parseBoolean(args[1]);
                    valid = true;
                    s.changeAssignRolesOnBuy(b);
                }

                catch (NumberFormatException err) {
                    e.getChannel().sendMessage("Please enter `true` pr `false`");
                }
            }

            EmbedBuilder eb = new EmbedBuilder();

            String state = s.getRoleAssignOnBuy() ? "on" : "off";

            eb.setTitle("Auto Assign Roles");
            eb.addField(new Field("The Auto Assign Roles is Now", "`" + state + "`", true));
            e.getChannel().sendMessage(eb.build()).queue();
        }
    }

    private void cooldown(GuildMessageReceivedEvent e, String[] args) 
    {
        cancelProgress(e);
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        if (args.length == 1) {
            e.getChannel().sendTyping().queue();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Cooldown");
            eb.addField(new Field("The Cooldown is Currently", "" + s.getWorkCooldown(), true));
            e.getChannel().sendMessage(eb.build()).queue();
        }

        else {
            if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
                return;

            e.getChannel().sendTyping().queue();
            boolean valid = false;
            while (!valid) {
                try {
                    int newCooldown = Integer.parseInt(args[1]);
                    valid = true;
                    s.changeCooldown(newCooldown);
                }

                catch (NumberFormatException err) {
                    e.getChannel().sendMessage("Please enter an integer");
                }
            }

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Cooldown");
            eb.addField(new Field("The Cooldown is Now", "" + s.getWorkCooldown(), true));
            e.getChannel().sendMessage(eb.build()).queue();
        }
    }

    @Override
    protected void help(GuildMessageReceivedEvent e) 
    {
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, false))
            return;

        EmbedBuilder eb = new EmbedBuilder();
        PrivateChannel c = e.getAuthor().openPrivateChannel().complete();

        eb.setTitle("Admin Only Commands");
        eb.setDescription(loadHelpMessage());
        c.sendMessage(eb.build()).queue();

        eb.setTitle("Admin Only Rank Commands");
        eb.setDescription(loadRankHelpMessage());
        c.sendMessage(eb.build()).queue();
    }

    private String loadRankHelpMessage() 
    {
        String out = "";
        String[] message = FileReader.read(AlloyUtil.ALLOY_PATH + "res\\globals\\help\\adminRankHelp.msg");
        for (String string : message)
            out += string + "\n";
        return out;
    }

    private String loadHelpMessage() 
    {
        String out = "";
        String[] message = FileReader.read(AlloyUtil.ALLOY_PATH + "res\\globals\\help\\adminHelp.msg");
        for (String string : message)
            out += string + "\n";
        return out;
    }

    private void day(GuildMessageReceivedEvent e) {
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;

        String path = getGuildPath(e.getGuild()) + "\\users";
        PlayerLoaderText plt = new PlayerLoaderText();
        List<Player> players = plt.loadALl(path);
        for (Player p : players)
            p.day();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Sucsess");
        eb.addField(new Field("The Day Has Adveanced", "the day has scsessfully advanced", true));
        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void cancelProgress(GuildMessageReceivedEvent e) 
    {
        boolean sendMessage = this.buildingB || this.removeBuilding;
        this.buildingB = false;
        this.building = null;
        this.removeBuilding = false;
        this.buildingConstrucort = null;
        this.removeWorkOption = false;

        if (sendMessage)
            cancelEmbed(e);
    }

    private void startingBalanceCommand(GuildMessageReceivedEvent e, String[] args) {
        cancelProgress(e);
        if (!hasPerm(e, DisPerm.ADMINISTRATOR, true))
            return;

        if (args.length == 1)
            viewStartingBalance(e);
        else 
            changeStartingBalance(e , args);
    }

    private void changeStartingBalance(GuildMessageReceivedEvent e, String[] args) 
    {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        e.getChannel().sendTyping().queue();
        try 
        {
            int newBal = Integer.parseInt( args[1] );
            s.changeStartingBalance(newBal);
        }
        catch (Exception ex) {
        }

        viewStartingBalance(e , "The Starting Balance is now $");
    }

    private void viewStartingBalance(GuildMessageReceivedEvent e) 
    {
        viewStartingBalance(e , "The Starting Balance is Currently $");
    }

    private void viewStartingBalance(GuildMessageReceivedEvent e, String m) 
    {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        EmbedBuilder eb = new EmbedBuilder();

        e.getChannel().sendTyping().queue();
        eb.setTitle("Starting Balance");
        eb.addField(new Field(m, "" + s.getStartingBalance(), true));
        e.getChannel().sendMessage(eb.build()).queue();

    }

    private void prefixCommand(GuildMessageReceivedEvent e, String[] args) 
    {
        cancelProgress(e);

        if (args.length == 1)
            viewPrefix(e);

        else if (hasPerm(e, DisPerm.ADMINISTRATOR, true))
            changePrefix(e , args);
    }

    private void viewPrefix(GuildMessageReceivedEvent e) 
    {
        viewPrefix(e , "The Prefix is Currently" );
    }

    private void changePrefix(GuildMessageReceivedEvent e, String[] args) 
    {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        s.changePrefix(args[1]);
        viewPrefix(e, "The Prefix is now");
    }

    private void viewPrefix(GuildMessageReceivedEvent e , String message) 
    {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        EmbedBuilder eb = new EmbedBuilder();

        e.getChannel().sendTyping().queue();
        eb.setTitle("Prefix");
        eb.addField(new Field( message , s.getPrefix(), true));
        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void selectRemoveBuilding(GuildMessageReceivedEvent e) 
    {
        //landbot\res\servers\example guild\settings\buildings.txt
        try 
        {
            BuildingLoaderText blt = new BuildingLoaderText();
            String bPath = getGuildPath(e.getGuild()) + "\\settings\\buildings.txt";
            List<Building> buildings = blt.loadALl(bPath);
            int rm = Integer.parseInt(e.getMessage().getContentRaw());
            Building b;
            ServerLoaderText slt = new ServerLoaderText();
            Server s = slt.load(getGuildPath(e.getGuild()));

            if (rm > 0 && rm <= buildings.size()) 
            {
                b = buildings.remove(rm - 1);
                String path = getGuildPath(e.getGuild()) + "\\users";
                PlayerLoaderText plt = new PlayerLoaderText();
                List<Player> players = plt.loadALl(path);
                this.removeBuildingFromPlayers( players , b, e);
            } 
            else
                e.getChannel().sendMessage("Please enter a valid number");

            this.writeBuildings(organizeBuildings(buildings), s);
            this.viewBuildings(e);
            this.removeBuilding = false;

        } catch (NumberFormatException ex) {
            e.getChannel().sendMessage("Please enter and Integer").queue();
        }
    }

    private void removeBuildingFromPlayers(List<Player> users, Building b, GuildMessageReceivedEvent e) 
    {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        for (Player p : users) 
        {
            p.removeBuilding(b);
            if (s.getRoleAssignOnBuy())
            {
                Role roll = e.getGuild().getRolesByName(b.getName(), true).get(0);
                e.getGuild().removeRoleFromMember(e.getMember() , roll).queue();
                roll.delete().queue();
            }
        }
    }

    private List<Building> organizeBuildings(List<Building> buildings) 
    {
        Collections.sort(buildings);
        return buildings;
    }

    private void removeBuilding(GuildMessageReceivedEvent e) 
    {
        cancelProgress(e);
        if (!hasPerm(e, DisPerm.ADMINISTRATOR , true))
            return;
        
        this.removeBuilding = true;
        this.buildingConstrucort = e.getAuthor();

        EmbedBuilder eb = new EmbedBuilder();
        BuildingLoaderText blt = new BuildingLoaderText();
        String bPath = getGuildPath(e.getGuild()) + "\\settings\\buildings.txt";
        List<Building> buildings = blt.loadALl(bPath);

        eb.setTitle("Choose Building to remove");
        embedBuildingListNumbered( eb , buildings );
        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void cancelEmbed(GuildMessageReceivedEvent e) 
    {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Cancel");
        eb.addField( new Field("Current progress has been cancelled ", "", true));
        e.getChannel().sendMessage(eb.build()).queue();
	}

	private void buildBuilding(String args, GuildMessageReceivedEvent e) 
    {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        if (this.building[0] == "")
            this.building[0] = args;
        
        else if ( this.building[1] == "" )
        {
            try 
            {
                this.building[1] = "" + Integer.parseInt(args);
            } 
            catch (Exception ex) 
            {
                e.getChannel().sendTyping().queue();
                e.getChannel().sendMessage("Please enter and Integer").queue();
            }
        }
        else if ( this.building[2] == "" )
        {
            try 
            {
                this.building[2] = "" + Integer.parseInt(args);
            } 
            catch (Exception ex) 
            {
                e.getChannel().sendTyping().queue();
                e.getChannel().sendMessage("Please enter and Integer").queue();
            }
        }

        boolean valid = this.building[0] != "" &&
                        this.building[1] != "" &&
                        this.building[2] != "";

        if (valid)
        {
            if (ableAddBuilding(e))
            {
                writeBuilding(s);
                writeBuildings( organizeBuildings(e) , s );
                embedCurrentBuilding(e);
                this.building = null;
                this.buildingB = false;
                this.buildingConstrucort = null;
                return;
            }
            else 
            {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Invalid");
                eb.addField(new Field("Too long", "what youve entered has made the list of building too long. please enter a shorter name or remove some buildings", true));
                e.getChannel().sendMessage(eb.build()).queue();
            }
        }

        embedCurrentBuilding(e);

    }

    private void writeBuilding(Server s) 
    {
        Building b = loadBuilding(this.building);
        String save = b.toString();
        String path = s.getPath() + "\\settings\\buildings.txt";

        Saver.saveAppend(path, save);
    }

    public static Building loadBuilding(String[] str) 
    {
		String name = str[0];
        int cost = Integer.parseInt(str[1]);
        int generation = Integer.parseInt(str[2]);

        BuildingSettings settings = new BuildingSettings();
        settings.setCost(cost)
                .setGeneration(generation)
                .setName(name);

        Building b = new Building( settings );
        return b;
	}

    private void writeBuildings(List<Building> list , Server s) 
    {
        String path = s.getPath() + "\\settings\\buildings.txt";
        String[] arr = new String[list.size()];
        for (int i = 0; i < arr.length; i++) 
            arr[i] = list.get(i).toString();
        
        Saver.saveOverwite(path, arr);

    }

    private List<Building> organizeBuildings(GuildMessageReceivedEvent e) 
    {
        BuildingLoaderText blt = new BuildingLoaderText();
        String bPath = getGuildPath(e.getGuild()) + "\\settings\\buildings.txt";
        List<Building> buildings = blt.loadALl(bPath);
        return this.organizeBuildings(buildings);
    }

    private void addBuilding(GuildMessageReceivedEvent e) 
    {
        cancelProgress(e);
        if (!hasPerm(e, DisPerm.ADMINISTRATOR , true))
            return;

        this.buildingB = true;
        this.building = new String[] {"","",""};
        this.buildingConstrucort = e.getAuthor();
        embedCurrentBuilding(e);
    }
    
    private boolean ableAddBuilding(GuildMessageReceivedEvent e) 
    {
        String names = "";
        String costs = "";
        String gener = "";
        BuildingLoaderText blt = new BuildingLoaderText();
        String bPath = getGuildPath(e.getGuild()) + "\\settings\\buildings.txt";
        List<Building> buildings = blt.loadALl(bPath);
        buildings.add (loadBuilding(this.building));
        
        for (Building b : buildings) 
        {
            names += "" + b.getName() + "\n";
            costs += "" + b.getCost() + "\n";
            gener += "" + b.getGeneration() + "\n";
        }
        
        boolean valid = names.length() <= 1024 &&
                        costs.length() <= 1024 &&
                        gener.length() <= 1024;
        
        return valid;
    }

    private void resetBuildings(GuildMessageReceivedEvent e) 
    {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));

        cancelProgress(e);
        if (!hasPerm(e, DisPerm.ADMINISTRATOR , true))
            return;

        removeAllBuildings(e);

        e.getChannel().sendTyping().queue();
        String path1 = AlloyUtil.ALLOY_PATH + "res\\globals\\defualt\\buildings.txt";
        String path2 = s.getPath() + "\\settings\\buildings.txt";
        
        Saver.copyFrom( path1 , path2 );
        viewBuildings(e);
    }

    private void removeAllBuildings(GuildMessageReceivedEvent e) 
    {
        BuildingLoaderText blt = new BuildingLoaderText();
        String bPath = getGuildPath(e.getGuild()) + "\\settings\\buildings.txt";
        List<Building> buildings = blt.loadALl(bPath);
        String path = getGuildPath(e.getGuild()) + "\\users";
        PlayerLoaderText plt = new PlayerLoaderText();
        List<Player> players = plt.loadALl(path);

        for (Building b : buildings) 
        {
            for (Player p : players)
                p.removeBuilding(b);
            List<Role> rolls = e.getGuild().getRolesByName(b.getName(), true);
            if (rolls.size() > 0)
                rolls.get(0).delete().queue();
        }
    }

    private void embedCurrentBuilding(GuildMessageReceivedEvent e)
    {
        e.getChannel().sendTyping().queue();
        EmbedBuilder eb = new EmbedBuilder();

        if (this.building[2] != "")
            eb.setTitle("Added new Building");
        else
            eb.setTitle("Add new Building");

        eb.addField(new Field("Name",       this.building[0] , true));
        eb.addField(new Field("Cost",       this.building[1] , true));
        eb.addField(new Field("Generation", this.building[2] , true));
        e.getChannel().sendMessage(eb.build()).queue();

    }

    private void viewBuildings(GuildMessageReceivedEvent e) 
    {
        EmbedBuilder eb = new EmbedBuilder();
        BuildingLoaderText blt = new BuildingLoaderText();
        String bPath = getGuildPath(e.getGuild()) + "\\settings\\buildings.txt";
        List<Building> buildings = blt.loadALl(bPath);

        eb.setTitle("All availible buildings");
        embedBuildingList(eb, buildings );
        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void embedBuildingList ( EmbedBuilder eb , List<Building> buildings )
    {
        String names = "";
        String costs = "";
        String gener = "";

        for (Building b : buildings) 
        {
            names += "" + b.getName() + "\n";
            costs += "" + b.getCost() + "\n";
            gener += "" + b.getGeneration() + "\n";

        }

        eb.addField(new Field( "Name" ,         names , true));
        eb.addField(new Field( "Cost" ,         costs , true));
        eb.addField(new Field( "Generation" ,   gener , true));

    }

    private void embedBuildingListNumbered ( EmbedBuilder eb , List<Building> buildings )
    {
        String numbr = "";
        String names = "";
        
        int i = 1;
        for (Building b : buildings) 
        {
            names += "" + b.getName() + "\n";
            numbr += "" + i + "\n";
            i++;
        }

        eb.addField(new Field( "#" ,    numbr , true));
        eb.addField(new Field( "Name" , names , true));
    }

    public void setGuilds(List<Guild> guilds) 
    {
        this.guilds = guilds;
    }

}
