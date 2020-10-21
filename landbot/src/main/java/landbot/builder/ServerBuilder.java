package landbot.builder;

import landbot.Server;
import landbot.io.FileReader;
import net.dv8tion.jda.api.entities.Guild;

public class ServerBuilder {

    public static Server buildServer( String guildname)
    {
        String path = "landbot\\res\\server\\" + guildname;
        String[] settings = FileReader.read(path +  "\\settings\\bot.settings");

        String[][] splitSettings = splitArgs(settings);

        String prefix = loadSetting(Server.PREFIX, splitSettings);
        String startString = loadSetting(Server.STARTING_BALANCE, splitSettings);
        String coolString = loadSetting(Server.COOLDOWN, splitSettings);
        String boolString1 = loadSetting(Server.ROLE_ASSIGN_ON_BUY, splitSettings);
        String boolString2 = loadSetting(Server.ADMIN_BYPASS_COOLDOWN, splitSettings);
        String spamString = loadSetting(Server.SPAM_CHANNEL, splitSettings);


        int start = Integer.parseInt(startString);
        int cool = Integer.parseInt(coolString);
        boolean roleAssign = Boolean.parseBoolean(boolString1);
        boolean adminBypass = Boolean.parseBoolean(boolString2);
        Long spamChannel = Long.parseLong(spamString);

        Server s = new Server(prefix, start, cool , path , roleAssign , adminBypass , spamChannel);

        return s;
    }

    public static Server buildServer(Guild g) 
    {
        String name = g.getName();
        return buildServer(name);
        
    }
    
    private static String loadSetting(String s, String[][] sArgs) 
    {
        for (String[] strings : sArgs) 
        {
            if (strings[0].equalsIgnoreCase(s))
                return strings[1];
        }
        return null;
    }

    private static String[][] splitArgs(String[] args) 
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

}
