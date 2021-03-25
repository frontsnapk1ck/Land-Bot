package frontsnapk1ck.alloy.handler.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import frontsnapk1ck.alloy.gameobjects.player.Building;
import frontsnapk1ck.alloy.gameobjects.player.Player;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.discord.DisUtil;
import frontsnapk1ck.alloy.utility.error.InvalidUserFormat;
import frontsnapk1ck.io.FileReader;
import frontsnapk1ck.io.Saver;
import frontsnapk1ck.utility.StringUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class EconHandler {

    public static final int MAX_LB_LENGTH = 10;
    public static final int MINIUM_BALANCE = 100;
    public static final int INVALID_FORMAT = -2143201685;

    public static boolean isSend(Message msg) 
    {
        return msg.getContentRaw().toLowerCase().contains("send");
    }

    public static boolean hasMessage(String[] args) 
    {
        return args.length > 2;
    }

    public static String getMessage(String[] args) 
    {
        String[] message = new String[args.length - 2];
        String out = "";

        int i = 0;
        for (String s : args) 
        {
            boolean v = getTargetUser(s) != null && getAmount(s) != INVALID_FORMAT;
            if (v)
                message[i] = s;
        }

        for (String string : message)
            out += string;

        return out;
    }

    public static User getTargetUser(String[] args) 
    {
        for (String s : args) {
            User u = getTargetUser(s);
            if (u != null)
                return u;
        }
        return null;
    }

    public static User getTargetUser(String s) 
    {
        try {
            User u = DisUtil.parseUser(s);
            return u;
        }

        catch (InvalidUserFormat e) {
            Alloy.LOGGER.debug("BankHandler", e);
        }
        return null;
    }

    public static int getAmount(String[] args) 
    {
        for (String s : args) {
            int num = getAmount(s);
            if (num != INVALID_FORMAT)
                return num;
        }
        return INVALID_FORMAT;

    }

    public static int getAmount(String s) 
    {
        try
        {
            int num = Integer.parseInt(s);
            return num;
        }
        catch (NumberFormatException e) 
        {
            return INVALID_FORMAT;
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

    public static boolean validName(String string, Guild g) 
    {
        List<Building> buildings = AlloyUtil.loadBuildings(g);
        for (Building building : buildings) 
        {
            if (building.getName().equalsIgnoreCase(string))
                return false;  
        }
        List<String> banned = AlloyUtil.getBlacklistedWords();
        for (String ban : banned) 
        {
            if (string.equalsIgnoreCase(ban))    
                return false;
        }
        return true;
    }

    public static boolean nameOutOfBounds(Building newB, List<Building> buildings) 
    {
        String names = "";
        String costs = "";
        String gener = "";
        buildings.add(newB);

        for (Building b : buildings) {
            names += "" + b.getName() + "\n";
            costs += "" + b.getCost() + "\n";
            gener += "" + b.getGeneration() + "\n";
        }

        boolean valid = names.length() <= 1024 && costs.length() <= 1024 && gener.length() <= 1024;

        return !valid;
    }

    public static void saveBuilding(Guild g, Building b) 
    {
        writeBuilding(b, g);
        writeBuildings(organizeBuildings(g), g);
    }

    private static void writeBuildings(List<Building> list, Guild g) 
    {
        String path = AlloyUtil.getGuildPath(g) + "\\settings\\buildings.txt";
        String[] arr = new String[list.size()];
        for (int i = 0; i < arr.length; i++)
            arr[i] = list.get(i).toString();

        Saver.saveOverwrite(path, arr);
    }

    private static void writeBuilding(Building b, Guild g) 
    {
        String save = b.toString();
        String path = AlloyUtil.getGuildPath(g) + "\\settings\\buildings.txt";

        Saver.saveAppend(path, save);
    }

    private static List<Building> organizeBuildings(Guild g) 
    {
        List<Building> buildings = AlloyUtil.loadBuildings(g);
        return organizeBuildings(buildings);
    }

    private static List<Building> organizeBuildings(List<Building> buildings) 
    {
        Collections.sort(buildings);
        return buildings;
    }

    public static Building removeBuilding(int rm, Guild g) throws IndexOutOfBoundsException 
    {
        List<Building> buildings = AlloyUtil.loadBuildings(g);
        Building b;

        if (rm >= 0 && rm < buildings.size()) 
        {
            b = buildings.remove(rm);
            if (AlloyUtil.loadServer(g).getRoleAssignOnBuy())
            {
                try 
                {
                    Role role = g.getRolesByName(b.getName(), true).get(0);
                    role.delete().complete();
                }
                catch (Exception e) 
                {
                    Alloy.LOGGER.warn("EconHandler", e.getMessage());
                }
            }
            writeBuildings(buildings, g);
            return b;
        }
        else
            throw new IndexOutOfBoundsException("" + rm + " is out of bounds for " + buildings.size());
    }

    public static void removeAllBuildings(Guild g) 
    {
        int max = AlloyUtil.loadBuildings(g).size();

        for (int i = 0; i < max; i++)
            removeBuilding(0, g);

    }

    public static void copyOverBuildings(Guild g) 
    {
        String defaultBuildings = AlloyUtil.GLOBAL_BUILDINGS_PATH;
        String currentBuildings = AlloyUtil.getGuildPath(g) + AlloyUtil.SETTINGS_FOLDER + AlloyUtil.SUB + AlloyUtil.BUILDING_FILE;

        Saver.copyFrom(defaultBuildings, currentBuildings);
        
    }

    public static boolean validBuildingName(String name, List<Building> buildings) 
    {
        for (Building b : buildings) 
        {
            if (b.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
	}

    public static Building getToBuy(String name, List<Building> buildings) 
    {
        for (Building b : buildings) 
        {
            if (b.getName().equalsIgnoreCase(name))
                return b;
        }
        return null;
	}

    public static Map<Building, Integer> getOwned(Member m) 
    {
        Map<Building , Integer > out = new HashMap<Building , Integer>();
        Player p = AlloyUtil.loadPlayer(m);
        Map<String, List<Building>> owned = p.getOwned();
        List<String> keys = p.getTypes();

        for (String s : keys) 
        {
            List<Building> bs = owned.get(s);
            if (bs.size() != 0) 
            {
                Building b = bs.get(0);
                out.put(b, bs.size());
            }
        }
        
        return out;
	}

    public static boolean canPay(Player from, int amount) 
    {
		return from.getBal() >= amount;
	}

    public static void pay(Player to, Player from, int amount) 
    {
        from.spend(amount);
        to.addBal(amount);
	}

    public static void addWorkOption(Guild g, String[] args) 
    {
        String out = StringUtil.joinStrings(args, 1);

        String path = AlloyUtil.getGuildPath(g) + "\\settings\\work.options";
        Saver.saveAppend(path, out);
    }

    public static String removeWork(int rm, Guild g) throws IndexOutOfBoundsException 
    {
        String path = AlloyUtil.getGuildPath(g) + "\\settings\\work.options";
        String[] options = FileReader.read(path);

        if (rm > 0 && rm <= options.length) 
        {
            String s = options[rm - 1].toString();
            options[rm - 1] = null;
            String[] newOp = new String[options.length - 1];

            int i = 0;
            for (String st : options) {
                if (st != null) {
                    newOp[i] = st;
                    i++;
                }
            }
            Saver.saveOverwrite(path, newOp);
            return s;
        }
        else
            throw new IndexOutOfBoundsException("" + rm + " is out of bounds for " + options.length);

    }

    public static void resetWork(Guild g) 
    {
        String to = AlloyUtil.getGuildPath(g) + AlloyUtil.SETTINGS_FOLDER + AlloyUtil.SUB + AlloyUtil.WORK_OPTIONS_FILE;
        String from = AlloyUtil.GLOBAL_WORK_OPTIONS_PATH;

        Saver.copyFrom(from, to);
    }
    
}
