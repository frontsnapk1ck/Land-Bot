package alloy.builder.loaders;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import alloy.builder.DataLoader;
import alloy.gameobjects.Warning;
import alloy.gameobjects.player.Account;
import alloy.gameobjects.player.Building;
import alloy.gameobjects.player.Player;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.settings.PlayerSettings;
import io.FileReader;

public class PlayerLoaderText extends DataLoader<Player, String> {

    @Override
    public Player load(String file) {
        AccountLoaderText alt = new AccountLoaderText();
        WarningLoaderText wlt = new WarningLoaderText();

        String accountPath = file   +   AlloyUtil.SUB + AlloyUtil.ACCOUNT_FILE;
        String buildingPath = file  +   AlloyUtil.SUB + AlloyUtil.BUILDING_FILE;
        String wanringsPath = file  +   AlloyUtil.WARNINGS_FOLDER;

        Account a = alt.load(accountPath);
        List<Warning> warnings = wlt.loadALl(wanringsPath);
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

    private List<String> getBuildingTypes(String path) 
    {
        List<String> out = new ArrayList<String>();
        String stockPath = loadStockPath(path);
        List<Building> stock = getStockBuildings(stockPath);
        
        for (Building building : stock) 
            out.add(building.getName());

        return out;
    }

    private int loadXP(String file) 
    {
        String path = file + AlloyUtil.SUB + AlloyUtil.RANK_FILE;
        String[] xp = FileReader.read(path);
        return Integer.parseInt(xp[0]);
    }

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

    private int findMax(String s, List<BuildingData> data) 
    {
        for (BuildingData d : data) 
        {
            if (d.name.equalsIgnoreCase(s))
                return d.count;
        }
        return -1;
    }

    private String loadStockPath(String path) 
    {
        String guildPath = path.substring(0, path.indexOf(AlloyUtil.USER_FOLDER));
        String outPath = guildPath + AlloyUtil.SETTINGS_FOLDER + AlloyUtil.SUB +  AlloyUtil.BUILDING_SETTINGS_FILE;
        return outPath;
    }

    private Building copyFromStock(List<Building> stock, String s) 
    {
        for (Building b : stock) 
        {
            if (b.getName().equalsIgnoreCase(s))
                return b.copy();
        }
        return null;
    }

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

    private List<Building> getStockBuildings(String path) 
    {
        BuildingLoaderText blt = new BuildingLoaderText();
        return blt.loadALl(path);
    }

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

    private class BuildingData 
    {
        public String   name;
        public int      count;

        public BuildingData( String name , int count)
        {
            this.name = name;
            this.count = count;
        }
    }

}
