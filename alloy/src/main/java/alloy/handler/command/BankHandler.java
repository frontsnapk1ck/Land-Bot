package alloy.handler.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import alloy.gameobjects.player.Player;
import alloy.main.Alloy;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.DisUtil;
import alloy.utility.error.InvalidUserFormat;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class BankHandler {

    public static final int MAX_LB_LENGTH = 10;
    public static final int MINIUM_BALANCE = 100;
    public static final int INVALID_FORMAT = -2143201685;

    public static boolean isSend(Message msg) {
        return msg.getContentRaw().toLowerCase().contains("send");
    }

    public static boolean hasMessage(String[] args) {
        return args.length > 2;
    }

    public static String getMessage(String[] args) {
        String[] message = new String[args.length - 2];
        String out = "";

        int i = 0;
        for (String s : args) {
            boolean v = getTargetUser(s) != null && getAmount(s) != BankHandler.INVALID_FORMAT;
            if (v) {
                message[i] = s;
            }
        }

        for (String string : message)
            out += string;

        return out;
    }

    public static User getTargetUser(String[] args) {
        for (String s : args) {
            User u = getTargetUser(s);
            if (u != null)
                return u;
        }
        return null;
    }

    public static User getTargetUser(String s) {
        try {
            User u = DisUtil.parseUser(s);
            return u;
        }

        catch (InvalidUserFormat e) {
            Alloy.LOGGER.debug("BankHandler", e);
        }
        return null;
    }

    public static int getAmount(String[] args) {
        for (String s : args) {
            int num = getAmount(s);
            if (num != BankHandler.INVALID_FORMAT)
                return num;
        }
        return BankHandler.INVALID_FORMAT;

    }

    public static int getAmount(String s) {
        try {
            int num = Integer.parseInt(s);
            return num;
        } catch (NumberFormatException e) {
            return BankHandler.INVALID_FORMAT;
        }
    }

    public static List<String> loadLeaderboardMoney(Guild g) 
    {
        List<Player> players = AlloyUtil.loadAllPlayers(g);
        List<String> positions = new ArrayList<String>();
        
        Comparator<Player> comparator = new Comparator<Player>()
        {
            public int compare(Player o1, Player o2) 
            {
                if (o1.getBal() > o2.getBal())
                    return -1;
                else if (o1.getBal() < o2.getBal())
                    return 1;
                return 0;
            };
        };
        Collections.sort(players, comparator);

        for (Player player : players) {
            if (positions.size() <= MAX_LB_LENGTH)
                positions.add(getLBRank(player));
        }

        return positions;
    }

    private static String getLBRank(Player player) 
    {
        int money = player.getBal();
        return "<@!" + player.getId() + ">\nmoney: `" + money + "`\n";
    }
    

}
