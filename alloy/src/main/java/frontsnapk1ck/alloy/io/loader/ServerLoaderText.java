package frontsnapk1ck.alloy.io.loader;

import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.alloy.gameobjects.RankUp;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.settings.ServerSettings;
import frontsnapk1ck.io.DataLoader;
import frontsnapk1ck.io.FileReader;

public class ServerLoaderText extends DataLoader<Server, String> {


    @Override
    public Server load(String file) 
    {
        String path = file + AlloyUtil.SETTINGS_FOLDER + AlloyUtil.SUB + AlloyUtil.BOT_SETTINGS_FILE;
        RankupLoaderText rult = new RankupLoaderText();
        String[] arr = FileReader.read(path);
        String[][] serverArray = configureServerArray(arr);

        String prefixString         = loadSetting(Server.PREFIX, serverArray);
        String startString          = loadSetting(Server.STARTING_BALANCE, serverArray);
        String coolString           = loadSetting(Server.COOLDOWN, serverArray);
        String boolString1          = loadSetting(Server.ROLE_ASSIGN_ON_BUY, serverArray);
        String boolString2          = loadSetting(Server.ADMIN_BYPASS_COOLDOWN, serverArray);
        String spamString           = loadSetting(Server.SPAM_CHANNEL, serverArray);
        String combinedBlackList    = loadSetting(Server.BLACKLISTED_CHANNELS, serverArray);
        String xpCooldownString     = loadSetting(Server.XP_COOLDOWN, serverArray);
        String idString             = loadSetting(Server.ID, serverArray);
        String userLogString        = loadSetting(Server.USER_LOG_CHANNEL , serverArray);
        String modLogString         = loadSetting(Server.MOD_LOG_CHANNEL, serverArray);
        String muteRoleString       = loadSetting(Server.MUTE_ROLE_ID, serverArray);
        String banAppealString      = loadSetting(Server.BAN_APPEAL_LINK, serverArray);
        String isLoadedString       = loadSetting(Server.IS_LOADED, serverArray);


        String prefix               = String.valueOf(prefixString);
        int start                   = Integer.parseInt(startString);
        int cool                    = Integer.parseInt(coolString);
        boolean roleAssign          = Boolean.parseBoolean(boolString1);
        boolean adminBypass         = Boolean.parseBoolean(boolString2);
        boolean loaded              = Boolean.parseBoolean(isLoadedString);
        Long spamChannel            = Long.parseLong(spamString);
        List<Long> blacklist        = loadBlacklist(combinedBlackList);
        int xpCooldown              = Integer.parseInt(xpCooldownString);
        List<RankUp> rankUps        = rult.loadALl(file + "/settings/rank.ups");
        long id                     = Long.parseLong(idString);
        long modLog                 = modLogString != null ? Long.parseLong(modLogString) : 0l;
        long userLog                = userLogString != null ? Long.parseLong(userLogString) : 0l;
        long muteRole               = muteRoleString != null ? Long.parseLong(muteRoleString) : 0l;

        ServerSettings settings = new ServerSettings();
        settings.setStartingBalance(start)
                .setCooldown(cool)
                .setRoleAssignOnBuy(roleAssign)
                .setAdminBypassCooldown(adminBypass)
                .setSpamChannel(spamChannel)
                .setBlacklistedChannels(blacklist)
                .setXpCooldown(xpCooldown)
                .setRankups(rankUps)
                .setId(id)
                .setModLogChannel(modLog)
                .setUserLogChannel(userLog)
                .setPrefix(prefix)
                .setMuteRole(muteRole)
                .setBanAppealLink(banAppealString)
                .setLoaded(loaded)
                .setPath(file);

        Server s = new Server( settings );

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

    private static String loadSetting(String s, String[][] serverArray) 
    {
        for (String[] strings : serverArray) 
        {
            if (strings[0].equalsIgnoreCase(s) && strings.length > 1)
                return strings[1];
        }
        return null;
    }

    @Override
    public List<Server> loadALl(String file) 
    {
        List<Server> servers = new ArrayList<Server>();
        String[] paths = FileReader.readFolderContents(file);
        for (String path: paths)
            servers.add(load(file + AlloyUtil.SUB + path));
        
        return servers;
    }
    
}
