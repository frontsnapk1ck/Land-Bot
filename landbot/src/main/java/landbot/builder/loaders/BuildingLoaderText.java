package landbot.builder.loaders;

import java.util.ArrayList;
import java.util.List;

import landbot.builder.DataLoader;
import landbot.gameobjects.player.Building;
import landbot.io.FileReader;

public class BuildingLoaderText extends DataLoader<Building, String> {

    @Override
    public List<Building> loadALl(String file) 
    {
        List<Building> buildings = new ArrayList<Building>();
        String[] sArr = FileReader.read(file);
        String[][] buildingArr = configureBuildingArray(sArr);

        for (String[] strings : buildingArr) 
            buildings.add(load(strings));
        
        return buildings;
    }

    private Building load(String[] s) 
    {
        String name = s[0];
        String costString = s[1];
        String genrString = s[2];

        int cost = Integer.parseInt(costString);
        int gern = Integer.parseInt(genrString);

        Building b = new Building(name, cost, gern);
        return b;
    }

    private String[][] configureBuildingArray(String[] sArr) 
    {
        String[][] buildingArr = new String[sArr.length][];

        for (int i = 0; i < sArr.length; i++) 
            buildingArr[i] = sArr[i].split(":");
        
        return buildingArr;
    }

    
}
