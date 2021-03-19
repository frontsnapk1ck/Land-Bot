package frontsnapk1ck.alloy.handler.command;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import frontsnapk1ck.alloy.command.util.PunishType;
import frontsnapk1ck.alloy.gameobjects.Case;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.gameobjects.Warning;
import frontsnapk1ck.alloy.gameobjects.player.Player;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.main.intefs.Moderator;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.discord.DisUtil;
import frontsnapk1ck.alloy.utility.settings.CaseSettings;
import frontsnapk1ck.alloy.utility.settings.WarningSettings;
import frontsnapk1ck.disterface.util.template.Template;
import frontsnapk1ck.io.FileReader;
import frontsnapk1ck.io.Saver;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import frontsnapk1ck.utility.StringUtil;

public class AdminHandler {

    public static int nextID(Guild g) 
    {
        List<Case> cases = AlloyUtil.loadAllCases(g);
        if (cases.size() == 0)
            return 1;
        Case last = cases.get(cases.size() - 1);
        return last.getNum() + 1;
    }

    public static Case buildCase(int caseID, User author, PunishType punishType, String message, Member targetUser,
            Message msg) {
        if (message.equals(""))
            message = "No reason provided";

        String path = getCasePath(msg.getGuild(), caseID);

        CaseSettings settings = new CaseSettings();
        settings.setIssuer(author.getIdLong()).setMessageId(msg.getIdLong()).setNum(caseID).setPunishType(punishType).setTarget(targetUser.getIdLong())
                .setReason(message).setPath(path);

        return new Case(settings);
    }

    private static String getCasePath(Guild guild, int caseID) {
        String path = AlloyUtil.getGuildPath(guild);
        path += AlloyUtil.CASE_FOLDER;
        path += AlloyUtil.SUB;
        path += caseID;
        path += AlloyUtil.CASE_EXT;
        return path;
    }

    public static MessageEmbed toEmbed(Case c) 
    {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Case " + c.getNum());
        eb.addField("Punish Type" , c.getPunishType().getKeyword() , true);
        eb.setDescription(c.getReason() + "\n\nissued by: <@!" + c.getIssuer() + ">");
        eb.setColor(c.getPunishType().getColor());
        return eb.build();
    }

    public static Case getCase(long idLong, String caseId) {
        try {
            int num = Integer.parseInt(caseId);
            List<Case> cases = AlloyUtil.loadAllCases(idLong);
            for (Case c : cases) {
                if (c.getNum() == num)
                    return c;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static void update(Case c) {

    }

    public static List<Case> allCases(Member target) 
    {
        Guild g = target.getGuild();
        List<Case> all = AlloyUtil.loadAllCases(g);
        List<Case> cases = new ArrayList<Case>();
        for (Case c : all) 
        {
            if (c.getTargetId() == target.getIdLong())
                cases.add(c);
        }
        return cases;
    }

    public static Warning newWarning(String[] args, TextChannel channel, Member warned, User warner, Guild guild) {
        checkWarningFiles(warned.getIdLong(), guild);
        List<String> ids = getAllIds(guild);
        String id = getNewID(ids);
        String message = StringUtil.joinStrings(args, 1);

        String warningPath = AlloyUtil.getWarningPath(warned.getIdLong(), guild) + AlloyUtil.SUB + id + AlloyUtil.WARNING_EXT;

        WarningSettings settings = new WarningSettings();
        settings.setId(id).setIssuer(warner.getIdLong()).setReason(message).setTarget(warned.getIdLong())
                .setPath(warningPath);

        Warning w = new Warning(settings);

        return w;
    }

    private static String getNewID(List<String> ids) {
        boolean valid = false;
        String id = "UNASSIGNED";

        while (!valid) {
            Random rand = new Random();
            int num1 = rand.nextInt(2147483647);
            int num2 = rand.nextInt(2147483647);
            id = Integer.toHexString(num1) + Integer.toHexString(num2);

            valid = true;
            for (String usedID : ids) {
                if (usedID.equalsIgnoreCase(id))
                    valid = false;
            }
        }

        return id;
    }

    private static List<String> getAllIds(Guild guild) {
        List<String> ids = new ArrayList<String>();

        String path = AlloyUtil.getGuildPath(guild) + AlloyUtil.USER_FOLDER;
        String[] users = FileReader.readFolderContents(path);
        for (String s : users) 
        {
            Long id = Long.parseLong(s);
            List<Warning> warnings = AlloyUtil.loadUserWarnings(id , guild);
            for (Warning w : warnings)
                ids.add(w.getId());
        }
        return ids;

    }

    public static void checkWarningFiles(long l, Guild guild) {
        String path = AlloyUtil.getWarningPath(l, guild);
        File f = new File(path);
        File gFile = new File(AlloyUtil.getPlayerPath(guild, l));
        if (!gFile.exists())
            return;

        if (!f.exists())
            Saver.saveNewFolder(f.getPath());
    }

    public static List<Warning> getWarnings(Guild g, long id) 
    {
        List<Warning> warnings = AlloyUtil.loadUserWarnings(id , g);
        return warnings;
    }

    public static boolean warningExists(Guild g, String warnID) {
        Map<Long, List<Warning>> warnings = getAllGuildWarnings(g);
        Set<Long> userIDs = warnings.keySet();

        for (Long uID : userIDs) {
            List<Warning> uWarnings = warnings.get(uID);
            for (Warning w : uWarnings) {
                if (w.getId().equalsIgnoreCase(warnID))
                    return true;
            }
        }

        return false;

    }

    public static Member removeWarnings(Guild g, String warnID) {
        Map<Long, List<Warning>> warnings = getAllGuildWarnings(g);
        Set<Long> userIDs = warnings.keySet();

        long idToRemove = -1l;
        Warning warnToRemove = null;

        for (Long uID : userIDs) {
            List<Warning> uWarnings = warnings.get(uID);
            for (Warning w : uWarnings) {
                if (w.getId().equalsIgnoreCase(warnID)) {
                    idToRemove = uID;
                    warnToRemove = w;
                }
            }
        }

        Player p = AlloyUtil.loadPlayer(g, idToRemove);

        p.removeWarning(warnToRemove);

        User u = User.fromId(idToRemove);
        Member m = g.getMember(u);

        return m;

    }

    private static Map<Long, List<Warning>> getAllGuildWarnings(Guild g) {
        Map<Long, List<Warning>> map = new HashMap<>();

        String[] users = FileReader.readFolderContents(AlloyUtil.getGuildPath(g) + AlloyUtil.USER_FOLDER);
        for (String s : users) 
        {
            long userID = Long.parseLong(s);
            List<Warning> warningsU = AlloyUtil.loadUserWarnings(userID ,g);

            map.put(userID, warningsU);
        }

        return map;
    }

    public static String makeWaringTable(List<Warning> warnings) {
        if (warnings.size() == 0)
            return ("No warnings here");

        String out = "";
        String fOut = "";
        for (Warning w : warnings)
            out += "*id*: " + w.getId() + "\t*issuer*:" + w.getIssuer() + "\n";
        out = out.trim();

        String[] lines = out.split("\n");

        int i = 0;
        for (String string : lines) {
            Warning w = warnings.get(i);
            fOut += w.getReason();
            fOut += "\n";
            fOut += string;
            fOut += "\n\n";
            i++;
        }

        return fOut;
    }

    public static void makeCase(Sendable bot , Moderator mod , PunishType punishType, TextChannel chan, Message msg , Member targetUser)
    {
        String[] args = AlloyInputUtil.getArgs(msg);
        User author = msg.getAuthor();
        Guild g = chan.getGuild();

        int caseID = AdminHandler.nextID(g);
        TextChannel modLog = mod.getModLogChannel(g.getIdLong());
        if (modLog != null) 
        {
            String message = StringUtil.joinStrings(args, 1);
            Case c = AdminHandler.buildCase(caseID, author, punishType , message, targetUser, msg);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(modLog);
            sm.setFrom(AdminHandler.class);
            sm.setMessage(AdminHandler.toEmbed(c));
            bot.send(sm);
        }

        if (modLog == null) 
        {
            Template t = Templates.modlogNotFound();
            SendableMessage sm = new SendableMessage();
            sm.setChannel(chan);
            sm.setFrom(AdminHandler.class);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String[] fixed = fixModLogArgs(args, g);
        Template log = Templates.moderationLog(chan, chan.getGuild(), author, punishType , fixed);
        Server s = AlloyUtil.loadServer(chan.getGuild());
        TextChannel tc = DisUtil.findChannel(chan.getGuild(), s.getModLogChannel());
        SendableMessage sm = new SendableMessage();
        sm.setChannel(tc);
        sm.setFrom(AdminHandler.class);
        sm.setMessage(log.getEmbed());
        bot.send(sm);
    }

    private static String[] fixModLogArgs(String[] args, Guild g) 
    {
        String[] newArr = new String[args.length];

        int i = 0;
        for (String s : args) {
            if (DisUtil.isRole(s, g))
                s = DisUtil.parseRole(s, g).getName();
            else if (DisUtil.isUserMention(s))
                s = DisUtil.findMember(g, s).getUser().getAsTag();
            else if (DisUtil.isValidChannel(g, s))
                s = DisUtil.findChannel(g, s).getName();
            newArr[i] = s;
            i++;
        }

        return newArr;
    }

}