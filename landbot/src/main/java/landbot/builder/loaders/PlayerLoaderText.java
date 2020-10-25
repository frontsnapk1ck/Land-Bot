package landbot.builder.loaders;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import landbot.builder.DataLoader;
import landbot.io.FileReader;
import landbot.player.Account;
import landbot.player.Building;
import landbot.player.Player;
import landbot.player.Rank;

public class PlayerLoaderText extends DataLoader<Player, String> {

    @Override
    public Player load(String file) {
        AccountLoaderText alt = new AccountLoaderText();
        RankLoaderText rlt = new RankLoaderText();

        String accountPath = file + "\\account.txt";
        String rankPath = file + "\\rank.txt";
        String buildingPath = file +"\\buildings.txt";

        Account a = alt.load(accountPath);
        Rank r = rlt.load(rankPath);
        long id = parseID(file);

        Player p = new Player(id, a, file, r);
        p.setHash(configureHashMap(buildingPath));

        return p;
    }

    private HashMap<String, List<Building>> configureHashMap(String path) 
    {
        HashMap<String, List<Building>> map = new HashMap<String, List<Building>>();
        List<Building> stock = getStockBuildings(path);
        buildHashMap(map , stock );
        
        String[] arr = FileReader.read(path);
        String[][] buildingArr = configureBuildingArray(arr);

        if (buildingArr == null)
            return map;

        int[]    counts = getCountArray(buildingArr); // new int[buildingArr.length];
        String[] names  = getNameArray(buildingArr); // new String[buildingArr.length];

        fillHashMap(map , stock , names , counts);


        return map;
    }

    private void fillHashMap(HashMap<String, List<Building>> map, List<Building> stock, String[] names, int[] counts) 
    {
        int i = 0;
        for (String s : map.keySet()) 
        {
            for (int j = 0; j < counts[i]; j++) 
            {
                Building b = copyFromStock(stock , s);
                map.get(s).add(b);    
            }
            
            i++;
        }
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

    private int[] getCountArray(String[][] buildingArr) 
    {
        int[] ints = new int[buildingArr.length];

        int i = 0;
        for (String[] s : buildingArr) 
        {
            ints[i] = Integer.parseInt(s[1]);   
            i++;
        }
        return ints;
    }

    private String[] getNameArray(String[][] buildingArr) 
    {
        String[] names = new String[buildingArr.length];

        int i = 0;
        for (String[] s : buildingArr) 
        {
            names[i] = s[0];
            i++;
        }
        return names;
    }

    private String[][] configureBuildingArray(String[] arr) 
    {
        if (arr.length == 0)
            return null;
        String[][] sArr = new String[arr.length][];
        
        int i = 0;
        for (String s : arr) 
        {
            sArr[i] = s.split(">");
            i++;    
        }
        
        return sArr;
    }

    private List<Building> getStockBuildings(String path) 
    {
        BuildingLoaderText blt = new BuildingLoaderText();

        String name = getGuildName(path);
        String guildPath = "landbot\\res\\servers\\" + name;
        String buildingsPath = guildPath + "\\settings\\buildings.txt";

        return blt.loadALl(buildingsPath);
    }

    private String getGuildName(String path) 
    {
        //landbot\\res\\servers\\example guild
        String name = path;
        for (int i = 0; i < 3; i++) 
        {
            int index = name.indexOf("\\");
            name = name.substring(index + 1);
        }
        int index = name.indexOf("\\");
        name = name.substring(0, index);
        return name;
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

    private long parseID(String file) 
    {
        // landbot\\res\\servers\\example server\\users\\example user.txt
        String name = file;
        for (int i = 0; i < 5; i++) 
        {
            int index = name.indexOf("\\");
            name = name.substring(index + 1);
        }
        return Long.parseLong(name); 
    }

    @Override
    public List<Player> loadALl(String file) 
    {
        List<Player> players = new ArrayList<Player>();
        File f = new File(file);
        if (!checkValidFile(f))
            return null;
        
        for (File sub : f.listFiles())
        {
            String path = sub.getPath();
            Player p = this.load(path);
            players.add(p);
        }
        return players;
       
    }

    private boolean checkValidFile(File f) 
    {
        if (f.isDirectory())
        {
            for (File sub : f.listFiles()) 
            {
                if (!sub.isDirectory())
                    return false;
            }
        }
        return true;
    }

}
