package alloy.io.loader;

import java.util.ArrayList;
import java.util.List;

import alloy.gameobjects.player.Building;
import alloy.utility.settings.BuildingSettings;
import io.DataLoader;
import io.FileReader;

/**
 * loader for loading buildings
 */
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

    /**
     * 
     * @param s a sting array of all the things needed to load a building
     * @return a building with the given values
     */
    private Building load(String[] s) 
    {
        String name = s[0];
        String costString = s[1];
        String genrString = s[2];

        int cost = Integer.parseInt(costString);
        int gern = Integer.parseInt(genrString);

        BuildingSettings settings = new BuildingSettings();
        settings.setCost(cost)
                .setGeneration(gern)
                .setName(name);

        Building b = new Building( settings );
        
        return b;
    }
    
    /**
     * 
     * @param args the array of newlines from the file
     * @return a {@link String}[][] that is properly split up   
     */
    private String[][] configureBuildingArray(String[] sArr) 
    {
        String[][] buildingArr = new String[sArr.length][];

        for (int i = 0; i < sArr.length; i++) 
            buildingArr[i] = sArr[i].split(":");
        
        return buildingArr;
    }

    
}
