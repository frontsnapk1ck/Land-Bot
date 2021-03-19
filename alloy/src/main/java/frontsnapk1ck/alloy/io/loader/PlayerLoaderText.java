package frontsnapk1ck.alloy.io.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import frontsnapk1ck.alloy.gameobjects.Warning;
import frontsnapk1ck.alloy.gameobjects.player.Account;
import frontsnapk1ck.alloy.gameobjects.player.Building;
import frontsnapk1ck.alloy.gameobjects.player.Player;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.settings.PlayerSettings;
import frontsnapk1ck.io.DataLoader;
import frontsnapk1ck.io.FileReader;

public class PlayerLoaderText extends DataLoader<Player, String> {

    @Override
    public Player load(String file)
    {
        AccountLoaderText alt = new AccountLoaderText();
        WarningLoaderText wlt = new WarningLoaderText();

        String accountPath = file   +   AlloyUtil.SUB + AlloyUtil.ACCOUNT_FILE;
        String buildingPath = file  +   AlloyUtil.SUB + AlloyUtil.BUILDING_FILE;
        String warningsPath = file  +   AlloyUtil.WARNINGS_FOLDER;

        Account a = alt.load(accountPath);
        List<Warning> warnings = wlt.loadALl(warningsPath);
        int xp = loadXP(file);
        long id = AlloyUtil.parseID(file);

        PlayerSettings settings = new PlayerSettings();
        settings.setAccount(a)
                .setXp(xp)
                .setId(id)
                .setPath(file)
                .setWanrings(warnings)
                .setOwned(configureHashMap(buildingPath))
                .setBuildingTypes(getBuildingTypes(buildingPath));

        Player p = new Player(settings);

          return p;
    }

    /**
     * 
     * @param path the path of the buildings to load building types from
     * @return a list of building types
     */
    private List<String> getBuildingTypes(String path) 
    {
        List<String> out = new ArrayList<String>();
        String stockPath = loadStockPath(path);
        List<Building> stock = getStockBuildings(stockPath);
        
        for (Building building : stock) 
            out.add(building.getName());

        return out;
    }

    /**
     * 
     * @param file the location of the players experience
     * @return the amount of xp the player has
     */
    private int loadXP(String file) 
    {
        String path = file + AlloyUtil.SUB + AlloyUtil.RANK_FILE;
        String[] xp = FileReader.read(path);
        return Integer.parseInt(xp[0]);
    }

    /**
     * 
     * @param path the location of the buildings that the player owns
     * @return a hashmap of building types to a list of buildings
     */
    private HashMap<String, List<Building>> configureHashMap(String path) 
    {
        HashMap<String, List<Building>> map = new HashMap<String, List<Building>>();
        String stockPath = loadStockPath(path);
        List<Building> stock = getStockBuildings(stockPath);
        buildHashMap(map , stock );
        
        String[] arr = FileReader.read(path);
        List<BuildingData> data = configureBuildingArray(arr);

        if (data == null)
            return map;

        fillHashMap(map , stock , data );


        return map;
    }

    /**
     * 
     * @param map the map that needs to be filled
     * @param stock the list of stock building in a server
     * @param data a list of building data used to determine the amount of buildings to load
     */
    private void fillHashMap(HashMap<String, List<Building>> map, List<Building> stock, List<BuildingData> data) 
    {
        for (String s : map.keySet()) 
        {
            int max = findMax(s, data);
            for (int j = 0; j < max; j++) 
            {
                Building b = copyFromStock(stock , s);
                map.get(s).add(b);    
            }
        }
    }

    /**
     * 
     * @param s the name of the building that is being look for
     * @param data the list of building data that is being searched though to find the number of buildings
     * @return the number of buildings that need to be created
     */
    private int findMax(String s, List<BuildingData> data) 
    {
        for (BuildingData d : data) 
        {
            if (d.name.equalsIgnoreCase(s))
                return d.count;
        }
        return -1;
    }

    /**
     * this is kinda stupid, but because of the way i have things rn, this is the way it has to be because i dont want to have to change things
     * @param path the path to the player's buildings
     * @return the path to the guild buildings
     */
    private String loadStockPath(String path) 
    {
        String guildPath = path.substring(0, path.indexOf(AlloyUtil.USER_FOLDER));
        String outPath = guildPath + AlloyUtil.SETTINGS_FOLDER + AlloyUtil.SUB +  AlloyUtil.BUILDING_SETTINGS_FILE;
        return outPath;
    }

    /**
     * calls {@link Building#copy()} and makes a new building
     * @param stock the list of stock building to compare against
     * @param s the name of the building to look for
     * @return a copy of that building
     */
    private Building copyFromStock(List<Building> stock, String s) 
    {
        for (Building b : stock) 
        {
            if (b.getName().equalsIgnoreCase(s))
                return b.copy();
        }
        return null;
    }

    /**
     * 
     * @param args the array of newlines from the file
     * @return a {@link String}[][] that is properly split up   
     */
    private List<BuildingData> configureBuildingArray(String[] arr) 
    {
        if (arr.length == 0)
            return null;
        List<BuildingData> data = new ArrayList<BuildingData>();
        
        for (String s : arr) 
        {
            String[] sArr = s.split(">");
            String name = sArr[0];
            int count = Integer.parseInt(sArr[1]);

            data.add( new BuildingData(name, count));
        }
        
        return data;
    }

    /**
     * 
     * @param path path of the stock building in this server
     * @return a list of default buildings for this server
     */
    private List<Building> getStockBuildings(String path) 
    {
        BuildingLoaderText blt = new BuildingLoaderText();
        return blt.loadALl(path);
    }

    /**
     * fills the map with blank lists based of the the stock buildings
     * @param map the maps that will be built
     * @param stock the list of stock buildings in the server
     */
    private void buildHashMap(HashMap<String, List<Building>> map, List<Building> stock) 
    {
        for (Building building : stock) 
        {
            String key = building.getName();
            List<Building> buildings = new ArrayList<Building>();
            map.put(key , buildings);    
        }
    }

    @Override
    public List<Player> loadALl(String file) 
    {
        List<Player> players = new ArrayList<Player>();
        File f = new File(file);
        
        for (File sub : f.listFiles())
        {
            String path = sub.getPath();
            Player p = this.load(path);
            players.add(p);
        }
        return players;
       
    }

    /**
     * holds all of the building data for how to load the hashmap of building data
     */
    private class BuildingData 
    {
        /**the name of the building */
        public String   name;
        /**the number of buildings that should be loaded */
        public int      count;

        public BuildingData( String name , int count)
        {
            this.name = name;
            this.count = count;
        }
    }

}
