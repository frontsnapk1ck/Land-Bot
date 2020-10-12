package landbot.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import landbot.io.FileReader;
import landbot.io.Saver;
import landbot.player.Building;
import landbot.player.Player;

public class PlayerBuilder {

    public static Player load(long playerID , String guildName) 
    {
        String path = "landbot\\res\\server\\" + guildName + "\\users\\" + playerID + ".txt";
        String[] arr = FileReader.read(path);
        String[][] sArr = new String[arr.length][2];
        
        if (arr.length == 0)
            handleBlank(path, guildName);
        
        for (int i = 0; i < sArr.length; i++) 
        {
            String[] tmp = arr[i].split(">");
            sArr[i][0] = tmp[0];
            if (tmp.length>1)
                sArr[i][1] = tmp[1];
        }

        int balance = Integer.parseInt(sArr[0][0]);
        Player p = new Player(playerID, balance , guildName);

        List<Building> stock = BuildingBuilder.loadBuildings(guildName);
        HashMap< String , List<Building>> owned = configureHashMap( stock );

        for (int i = 1; i < sArr.length; i++) 
        {
            String name = sArr[i][0];
            int num = Integer.parseInt(sArr[i][1]);

            List<Building> bs = owned.get(name);

            for (int j = 0; j < num; j++)
                bs.add(getBuildingByName(stock , name));
        }

        p.setHash(owned);
        return p;
    }
    
    
    public static List<Player> getAllPlayers(String guildName) 
    {
        List<Player> users = new ArrayList<Player>();
        String[] arr = FileReader.readFolderContents("landbot\\res\\server\\" + guildName + "\\users");
        String[] woEXT = new String[arr.length];

        int j = 0;
        for (String string : arr) 
        {
            int i = string.indexOf(".");
            String tmp = string.substring(0, i);
            woEXT[j] = tmp;
            j++;
        }
        
        for (String ss : woEXT) 
        {
            long id = Long.parseLong(ss);
            Player p = PlayerBuilder.load(id , guildName);
            users.add(p);
        }

        return users;
    }

    private static Building getBuildingByName(List<Building> bs, String name) 
    {
        for (Building b : bs) 
        {
            if (b.getName().equals(name))
                return b.copy();    
        }
        return null;
    }

    private static HashMap<String, List<Building>> configureHashMap(List<Building> stock) 
    {
        HashMap<String, List<Building>> map = new HashMap<String, List<Building>>();
        for (int i = 0; i < stock.size(); i++) 
        {
            Building b = stock.get(i);
            map.put(b.getName(), new ArrayList<>()); 
        } 
        
        return map;
    }

    private static void handleBlank(String path , String guildName) 
    {
        int balance = ServerBuilder.buildServer(guildName).getStartingBalance();


        String[] out = { "" + balance
        };

        Saver.saveOverwite(path, out);
    }
    
}
