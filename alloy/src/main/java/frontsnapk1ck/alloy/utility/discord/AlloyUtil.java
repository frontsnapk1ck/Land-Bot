package frontsnapk1ck.alloy.utility.discord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import frontsnapk1ck.alloy.audio.GuildMusicManager;
import frontsnapk1ck.alloy.gameobjects.Case;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.gameobjects.Warning;
import frontsnapk1ck.alloy.gameobjects.collection.BuildingCollection;
import frontsnapk1ck.alloy.gameobjects.collection.CaseCollection;
import frontsnapk1ck.alloy.gameobjects.collection.PlayerCollection;
import frontsnapk1ck.alloy.gameobjects.collection.RankCollection;
import frontsnapk1ck.alloy.gameobjects.collection.WarningCollection;
import frontsnapk1ck.alloy.gameobjects.player.Building;
import frontsnapk1ck.alloy.gameobjects.player.Player;
import frontsnapk1ck.alloy.gameobjects.player.Rank;
import frontsnapk1ck.alloy.io.loader.BuildingLoaderText;
import frontsnapk1ck.alloy.io.loader.CaseLoaderText;
import frontsnapk1ck.alloy.io.loader.PlayerLoaderText;
import frontsnapk1ck.alloy.io.loader.RankLoaderText;
import frontsnapk1ck.alloy.io.loader.ServerLoaderText;
import frontsnapk1ck.alloy.io.loader.WarningLoaderText;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.utility.AlloyCache;
import frontsnapk1ck.alloy.utility.discord.paths.AlloyFiles;
import frontsnapk1ck.alloy.utility.discord.paths.AlloyImages;
import frontsnapk1ck.disterface.util.template.Template;
import frontsnapk1ck.io.FileReader;
import frontsnapk1ck.utility.StringUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class AlloyUtil implements AlloyFiles, AlloyImages {

    private static AlloyCache cache;

    //+======================================
    //|
    //|     UTIL
    //|
    //+======================================

    //+======================================
    //|     message
    //+======================================

    public static void send(Template t , MessageChannel destination , Sendable send)
    {
        AlloyUtil.send(t, destination , send , SendType.EMBED );
    }
    
    private static void send(Template t, MessageChannel destination, Sendable send, SendType sendType)
    {
        SendableMessage sm = new SendableMessage();

        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String oldC = trace[1].getClassName();

        if (sendType == SendType.EMBED)
            sm.setMessage(t.getEmbed());
        if (sendType == SendType.STRING)
            sm.setMessage(t.getText());

        sm.setChannel(destination);
        try 
        {
            sm.setFrom(ClassLoader.getSystemClassLoader().loadClass(oldC));
        }
        catch (ClassNotFoundException e) 
        {
            Alloy.LOGGER.debug("AlloyUtil", e);
            sm.setFrom((Class<?>)null);
        }
        send.send(sm);
    }

    //+======================================
    //|
    //|     PATHS
    //|
    //+======================================


    public static String getGuildPath(Guild guild) 
    {
        return getGuildPath(guild.getIdLong());
    }

    private static String getGuildPath(long id) 
    {
        return AlloyUtil.SERVERS_PATH + AlloyUtil.SUB + id;
    }

    public static String getWarningPath(long l, Guild guild) 
    {
        return AlloyUtil.getGuildPath(guild) + AlloyUtil.USER_FOLDER + AlloyUtil.SUB + l + AlloyUtil.WARNINGS_FOLDER;
    }

    public static String getPlayerPath(Guild guild, Long id) 
    {
        return getGuildPath(guild) + USER_FOLDER + SUB + id;
    }

    public static String getMemberPath(Member target) 
    {
        String out = getGuildPath(target.getGuild());
        out += USER_FOLDER + SUB;
        out += target.getId();
        return out;
    }

    //+======================================
    //|
    //|     CACHE
    //|
    //+======================================

    public static AlloyCache getCache() 
    {
		return cache;
    }
    
    public static void loadCache() 
    {
        cache = new AlloyCache();
    }

    //+======================================
    //|     loaders
    //+======================================

    public static Server loadServer(Guild guild) 
    {
        String path = getGuildPath(guild);
        if (!cache.has(path))
        {
            ServerLoaderText slt = new ServerLoaderText();
            Server s = slt.load(path);
            cache.put(path , s);
        }
        return (Server) cache.get(path).getData();
    }

    public static List<Building> loadBuildings(Guild g) 
    {
        String bPath = AlloyUtil.getGuildPath(g) + AlloyUtil.SUB + AlloyUtil.SETTINGS_FOLDER + AlloyUtil.SUB + AlloyUtil.BUILDING_FILE;
        if (!cache.has(bPath))
        {
            BuildingLoaderText blt = new BuildingLoaderText();
            BuildingCollection collection = new BuildingCollection(blt.loadALl(bPath));
            cache.put(bPath, collection);
        }
        BuildingCollection collection = (BuildingCollection) cache.get(bPath);
        return collection.getList();
    }

    public static Player loadPlayer(Member m) 
    {
        return loadPlayer(m.getGuild(), m);
    }

    public static Player loadPlayer(Guild g, Member m) 
    {
        return loadPlayer(g, m.getIdLong());
    }

    public static Player loadPlayer(Guild g, long id) 
    {
        String path = getGuildPath(g) + USER_FOLDER + SUB + id;
        if (!cache.has(path))
        {
            PlayerLoaderText plt = new PlayerLoaderText();
            Player p = plt.load(path);
            cache.put(path, p);
        }
        return (Player) cache.get(path);
    }

    public static List<Player> loadAllPlayers(Guild g) 
    {
        String path = getGuildPath(g) + USER_FOLDER + SUB;
        if (!cache.has(path))
        {
            PlayerLoaderText plt = new PlayerLoaderText();
            
            List<Player> players = plt.loadALl(path);
            List<Player> toRm = new ArrayList<Player>();
            JDA jda = g.getJDA();
            for (Player player : players) 
            {
                User u = jda.getUserById(player.getId());
                if (u != null && u.isBot())
                    toRm.add(player);
            }
            players.removeAll(toRm);

            PlayerCollection collection = new PlayerCollection( players );
            cache.put(path, collection);
        }
        PlayerCollection collection = (PlayerCollection) cache.get(path);
        return collection.getList();
    }

    public static List<Case> loadAllCases(Guild g) 
    {
        return loadAllCases(g.getIdLong());
	}

    public static List<Case> loadAllCases(long id) 
    {
        String path = getGuildPath(id) + CASE_FOLDER;
        if (!cache.has(path))
        {        
            CaseLoaderText clt = new CaseLoaderText();
            CaseCollection collection = new CaseCollection(clt.loadALl(path));
            cache.put(path, collection);
        }
        CaseCollection collection = (CaseCollection) cache.get(path);
        return collection.getList();
    }
    
    public static List<Rank> loadAllGlobalRanks() 
    {
        if (!cache.has(GLOBAL_RANK_PATH))
        {
            RankLoaderText rlt = new RankLoaderText();
            RankCollection collection = new RankCollection(rlt.loadALl(GLOBAL_RANK_PATH));
            cache.put(GLOBAL_RANK_PATH, collection);
        }
        RankCollection collection = (RankCollection) cache.get(GLOBAL_RANK_PATH);
        return collection.getList();
    }

    public static List<Warning> loadUserWarnings(Long id, Guild guild) 
    {
        String path = getWarningPath(id, guild);
        if (!cache.has(path))
        {
            WarningLoaderText wlt = new WarningLoaderText();
            WarningCollection collection = new WarningCollection(wlt.loadALl(path));
            cache.put(path, collection);
        }
        WarningCollection collection = (WarningCollection) cache.get(path);
        return collection.getList();
    }
    
    //+======================================
    //|
    //|     GETTER
    //|
    //+======================================

    public static Member getMember(Player p, JDA jda) 
    {
        String id = p.getPath().split("\\\\")[8];
        Guild g = jda.getGuildById(id);
        return g.getMemberById(p.getId());

	}

    public static Member getMember(Guild g, Player p) 
    {
        long id = p.getId();
        User u = User.fromId(id);
        return g.getMember(u);
    }

    public static Guild getGuild(Server server, JDA jda) 
    {
        long id = server.getId();
        Guild g = jda.getGuildById(id);
        return g;
    }

    public static List<Long> getWhitelisted() 
    {
        String path = AlloyUtil.ALLOY_PATH + "res\\perms\\white.listed";
        String[] sArr = FileReader.read(path);
        List<Long> list = new ArrayList<>();
        for (String id : sArr) {
            try {
                list.add(Long.parseLong(id));
            } catch (NumberFormatException e) {
            }
        }
        return list;
    }

    public static List<Long> getBlacklisted() 
    {
        String path = AlloyUtil.ALLOY_PATH + "res\\perms\\black.listed";
        String[] sArr = FileReader.read(path);
        List<Long> list = new ArrayList<>();
        for (String id : sArr) {
            try {
                list.add(Long.parseLong(id));
            } catch (NumberFormatException e) {
            }
        }
        return list;
    }

    public static Member getMember(Guild g, String ping) 
    {
        ping = ping.replace("<@!", "");
        ping = ping.replace("<@", "");
        ping = ping.replace(">", "");

        return g.getMemberById(ping);
    }

    public static long parseID(String file) {
        String name = file;
        int numSlash = StringUtil.getNumCharsInString(name, "\\");
        for (int i = 0; i < numSlash; i++) {
            int index = name.indexOf(AlloyUtil.SUB);
            name = name.substring(index + 1);
        }
        return Long.parseLong(name);
    }
    
    public static List<String> loadWorkOptions(Server s) {
        List<String> workOptions = new ArrayList<String>();
        String path = s.getPath();
        path += SETTINGS_FOLDER + SUB + WORK_OPTIONS_FILE;
        String[] arr = FileReader.read(path);
        for (String string : arr)
            workOptions.add(string);
        return workOptions;
    }

    public static boolean isBlackListed(User author) 
    {
        long id = author.getIdLong();
        List<Long> blacklisted = getBlacklisted();
        for (Long l : blacklisted) 
        {
            if (l == id)
                return true;
        }
        return false;
    }
    
    public static List<Server> loadAllServers() 
    {
        String path = AlloyUtil.SERVERS_PATH;
        ServerLoaderText slt = new ServerLoaderText();
        return slt.loadALl(path);
	}

    public enum SendType {
        STRING,
        EMBED,
    }

    public static List<String> getBlacklistedWords() 
    {
        String[] arr = FileReader.read(GLOBAL_BLACKLISTED_WORDS);
        return Arrays.asList(arr);
    }

    public static String getVersion() 
    {
        File xmlFile = new File(POM_FILE);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            Element e = doc.getDocumentElement();
            NodeList children = e.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) 
            {
                Node child = children.item(i);
                if (child.getNodeName().equals("version"))
                    return child.getTextContent();
            }

        } catch (ParserConfigurationException | SAXException | IOException e) 
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void audioStopped(GuildMusicManager gmm) 
    {
        String id = gmm.getGuild().getId();
        id = "GMM: " + id;
        cache.put( id , gmm , AlloyCache.GMM_KEEP_TIME);
    }

    public static void audioStarted(GuildMusicManager gmm) 
    {
        String id = gmm.getGuild().getId();
        id = "GMM: " + id;
        cache.remove(id);
    }

}
