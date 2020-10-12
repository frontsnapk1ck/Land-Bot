package landbot.builder;

import java.util.ArrayList;
import java.util.List;

import landbot.io.FileReader;
import landbot.player.Building;

public class BuildingBuilder {

    public static List<Building> loadBuildings(String guildName) 
    {
        List<Building> buildings = new ArrayList<Building>();
        List<String[]> bSplits = new ArrayList<String[]>();

        
        String[] sArr = FileReader.read("landbot\\res\\server\\" + guildName + "\\settings\\buildings.txt");
            
        for (String string : sArr) 
            bSplits.add(string.split(":"));
        
        for (String[] str : bSplits) 
        {
            String name = str[0];
            int cost = Integer.parseInt(str[1]);
            int generation = Integer.parseInt(str[2]);

            Building b = new Building(name, cost, generation);
            buildings.add(b);
        }

        return buildings;
    }

    public static Building loadBuilding(String[] str) 
    {
		String name = str[0];
        int cost = Integer.parseInt(str[1]);
        int generation = Integer.parseInt(str[2]);

        Building b = new Building(name, cost, generation);
        return b;
	}

    public static Building loadBuilding(String string , String guildName) 
    {
        List<Building> bs = loadBuildings(guildName);
        for (Building building : bs) 
        {
            if (building.getName().equalsIgnoreCase(string))
                return building;
        }
        return null;
	}    
}
