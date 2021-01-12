package alloy.handler;

import java.util.Collections;
import java.util.List;

import alloy.gameobjects.Server;
import alloy.gameobjects.player.Building;
import alloy.gameobjects.player.Player;
import alloy.io.loader.BuildingLoaderText;
import alloy.io.loader.PlayerLoaderText;
import alloy.utility.discord.AlloyUtil;
import io.Saver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class BuildingHandeler {

    public static boolean validName(String string) 
    {
        return true;
	}

    public static boolean nameOutOfBounds(Building newB, List<Building> buildings) 
    {
        String names = "";
        String costs = "";
        String gener = "";
        buildings.add(newB);

        for (Building b : buildings) 
        {
            names += "" + b.getName() + "\n";
            costs += "" + b.getCost() + "\n";
            gener += "" + b.getGeneration() + "\n";
        }
        
        boolean valid = names.length() <= 1024 &&
                        costs.length() <= 1024 &&
                        gener.length() <= 1024;
        
        return !valid;
	}

    public static void saveBuilding(Guild g, Building b) 
    {
        writeBuilding( b , g);
        writeBuildings( organizeBuildings(g) , g );
	}

    private static void writeBuildings(List<Building> list, Guild g) 
    {
        String path = AlloyUtil.getGuildPath(g) + "\\settings\\buildings.txt";
        String[] arr = new String[list.size()];
        for (int i = 0; i < arr.length; i++) 
            arr[i] = list.get(i).toString();
        
        Saver.saveOverwite(path, arr);
    }

    private static void writeBuilding(Building b, Guild g) 
    {
        String save = b.toString();
        String path = AlloyUtil.getGuildPath(g) + "\\settings\\buildings.txt";

        Saver.saveAppend(path, save);
    }

    private static List<Building> organizeBuildings( Guild g )
    {
        BuildingLoaderText blt = new BuildingLoaderText();
        String bPath = AlloyUtil.getGuildPath(g) + "\\settings\\buildings.txt";
        List<Building> buildings = blt.loadALl(bPath);
        return organizeBuildings(buildings);
    }

    private static List<Building> organizeBuildings(List<Building> buildings) 
    {
        Collections.sort(buildings);
        return buildings;
    }

    public static Building removeBuilding(int rm, Guild g) throws IndexOutOfBoundsException
    {
        BuildingLoaderText blt = new BuildingLoaderText();
        String bPath = AlloyUtil.getGuildPath(g) + "\\settings\\buildings.txt";
        List<Building> buildings = blt.loadALl(bPath);
        Building b;

        if (rm >= 0 && rm < buildings.size()) 
        {
            b = buildings.remove(rm);
            String path = AlloyUtil.getGuildPath(g) + "\\users";
            PlayerLoaderText plt = new PlayerLoaderText();
            List<Player> players = plt.loadALl(path);
            removeBuildingFromPlayers( players , b, g);
            writeBuildings(buildings , g);
            return b;
        }
        else
            throw new IndexOutOfBoundsException("" + rm + " is out of bounds for " + buildings.size());
	}

    private static void removeBuildingFromPlayers(List<Player> players, Building b, Guild g) 
    {
        Server s = AlloyUtil.loadServer(g);

        for (Player p : players) 
        {
            p.removeBuilding(b);
            if (s.getRoleAssignOnBuy())
            {
                Role roll = g.getRolesByName(b.getName(), true).get(0);
                Member m = AlloyUtil.getMember(g , p);
                g.removeRoleFromMember(m , roll).queue();
                roll.delete().queue();
            }
        }  
    }

    public static void removeAllBuildings(Guild g) 
    {
        BuildingLoaderText blt = new BuildingLoaderText();
        String bPath = AlloyUtil.getGuildPath(g) + "\\settings\\buildings.txt";
        List<Building> buildings = blt.loadALl(bPath);

        for (int i = 0; i < buildings.size(); i++)
            removeBuilding(0, g);
        
	}

    public static void copyOverBuildings(Guild g) 
    {
        String defaultBuildgins = AlloyUtil.ALLOY_PATH + "res\\globals\\defualt\\buildings.txt";
        String currentBuildings = AlloyUtil.getGuildPath(g) + 
                                    AlloyUtil.SUB + 
                                    AlloyUtil.SETTINGS_FOLDER + 
                                    AlloyUtil.SUB + 
                                    AlloyUtil.BUILDING_FILE;

        Saver.copyFrom(defaultBuildgins, currentBuildings);

    }
    
}
