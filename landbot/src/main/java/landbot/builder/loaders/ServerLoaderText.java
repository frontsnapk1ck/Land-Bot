package landbot.builder.loaders;

import java.util.ArrayList;
import java.util.List;

import landbot.builder.DataLoader;
import landbot.gameobjects.RankUp;
import landbot.gameobjects.Server;
import landbot.io.FileReader;

public class ServerLoaderText extends DataLoader<Server, String> {


    @Override
    public Server load(String file) 
    {
        RankupLoaderText rult = new RankupLoaderText();
        String path = "landbot\\res\\servers\\" + getGuildName(file);
        String[] arr = FileReader.read(path +  "\\settings\\bot.settings");
        String[][] serverArray = configureServerArray(arr);

        String prefix = loadSetting(Server.PREFIX, serverArray);
        String startString = loadSetting(Server.STARTING_BALANCE, serverArray);
        String coolString = loadSetting(Server.COOLDOWN, serverArray);
        String boolString1 = loadSetting(Server.ROLE_ASSIGN_ON_BUY, serverArray);
        String boolString2 = loadSetting(Server.ADMIN_BYPASS_COOLDOWN, serverArray);
        String spamString = loadSetting(Server.SPAM_CHANNEL, serverArray);
        String combinedBlackList = loadSetting(Server.BALCKLISTED_CHANNENLS, serverArray);
        String xpCooldownString = loadSetting(Server.XP_COOLDOWN, serverArray);


        int start = Integer.parseInt(startString);
        int cool = Integer.parseInt(coolString);
        boolean roleAssign = Boolean.parseBoolean(boolString1);
        boolean adminBypass = Boolean.parseBoolean(boolString2);
        Long spamChannel = Long.parseLong(spamString);
        List<Long> blacklist = loadBlacklist(combinedBlackList);
        int xpCooldown = Integer.parseInt(xpCooldownString);
        List<RankUp> rankUps = rult.loadALl(path + "\\settings\\rank.ups");

        Server s = new Server(prefix, start, cool , path , roleAssign , adminBypass , spamChannel , blacklist , xpCooldown , rankUps);

        return s;
    }

    private List<Long> loadBlacklist(String combinedBlackList) 
    {
        List<Long> out = new ArrayList<Long>();

        if (combinedBlackList == null)
            return out;

        String[] channels = combinedBlackList.split(";");
        for (String s : channels) 
            out.add(Long.parseLong(s));
        
        return out;
    }

    private String[][] configureServerArray(String[] args) 
    {
        String[][] sArgs = new String[args.length][];
        for (int i = 0; i < sArgs.length; i++) 
        {
            String s = args[i];
            String[] split = s.split(":");
            sArgs[i]= new String[split.length];
            for (int j = 0; j < split.length; j++) 
                sArgs[i][j] = split[j];
        }
        return sArgs;
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
        return name;
    }

    private static String loadSetting(String s, String[][] serverArray) 
    {
        for (String[] strings : serverArray) 
        {
            if (strings[0].equalsIgnoreCase(s) && strings.length > 1)
                return strings[1];
        }
        return null;
    }
    
}
