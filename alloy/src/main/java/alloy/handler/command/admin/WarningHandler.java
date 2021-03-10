package alloy.handler.command.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import alloy.gameobjects.Warning;
import alloy.gameobjects.player.Player;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.settings.WarningSettings;
import io.FileReader;
import io.Saver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.StringUtil;

public class WarningHandler {

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

}
