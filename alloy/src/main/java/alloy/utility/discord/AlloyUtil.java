package alloy.utility.discord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import alloy.builder.loaders.BuildingLoaderText;
import alloy.builder.loaders.PlayerLoaderText;
import alloy.builder.loaders.ServerLoaderText;
import alloy.gameobjects.Server;
import alloy.gameobjects.player.Building;
import alloy.gameobjects.player.Player;
import alloy.utility.discord.paths.AlloyExtentions;
import alloy.utility.discord.paths.AlloyImages;
import alloy.utility.discord.paths.AlloyPaths;
import io.FileReader;
import io.Saver;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.StringUtil;

public class AlloyUtil implements AlloyPaths, AlloyExtentions , AlloyImages {
    
    public static List<Building> loadBuildings(Guild g) 
    {
        BuildingLoaderText blt = new BuildingLoaderText();
        String bPath = getGuildPath(g)+ "\\settings\\buildings.txt";
        List<Building> buildings = blt.loadALl(bPath);
        return buildings;
    }
    
    public static void checkFileSystem( long guildID , TextChannel defaultChannel )
    {
        File dir = new File(AlloyUtil.ALLOY_PATH + "res\\servers\\" + guildID);
        if (!dir.exists()) 
        {
            dir.mkdir();
            File u = new File( dir.getPath() + "\\users");
            File s = new File( dir.getPath() + "\\settings");
            File c = new File( dir.getPath() + "\\cases");

            File buS = new File( s.getPath() + "\\buildings.txt");
            File boS = new File( s.getPath() + "\\bot.settings");
            File woO = new File( s.getPath() + "\\work.options");
            File luA = new File( s.getPath() + "\\rank.ups");

            u.mkdir();
            s.mkdir();
            c.mkdir();
            try 
            {
                buS.createNewFile();
                boS.createNewFile();
                woO.createNewFile();
                luA.createNewFile();

                String defaultChannelS = defaultChannel.getId();
                String[] boSS = {   Server.PREFIX + ":!" , 
                                    Server.STARTING_BALANCE + ":1000" ,
                                    Server.COOLDOWN + ":10" ,
                                    Server.ROLE_ASSIGN_ON_BUY + ":false",
                                    Server.SPAM_CHANNEL + ":" + defaultChannelS ,
                                    Server.BALCKLISTED_CHANNENLS + ":" , 
                                    Server.XP_COOLDOWN + ":4" ,
                                    Server.ID + ":" + guildID , 
                                    Server.MOD_LOG_CHANNEL + ":" ,
                                    Server.USER_LOG_CHANNEL + ":" ,
                                    Server.MUTE_ROLE_ID + ":" ,
                                };

                Saver.saveOverwite(boS.getPath(), boSS);

                String globalBuildings = AlloyUtil.ALLOY_PATH + "res\\globals\\defualt\\buildings.txt";
                Saver.copyFrom(globalBuildings, buS.getPath());

                String globalWork = AlloyUtil.ALLOY_PATH + "res\\globals\\defualt\\workOptions.txt";
                Saver.copyFrom(globalWork, woO.getPath());

            }

            catch (IOException ex) {
                ex.printStackTrace();
            }
            
        }
    }

    public static Server loadServer(Guild guild) 
    {
        ServerLoaderText slt = new ServerLoaderText();
        return slt.load(getGuildPath(guild));
    }

    public static String getGuildPath(Guild guild) 
    {
        return AlloyUtil.SERVERS_PATH + AlloyUtil.SUB + guild.getId();
    }

    public static String getPlayerPath( Guild guild , Long id)
    {
        return getGuildPath(guild) + "\\users\\" + id;
    }

    public static Member getMember(Guild g, Player p) 
    {
        long id = p.getId();
        User u = User.fromId(id);
        return g.getMember(u);
	}

    public static void checkFileSystem(Guild g) 
    {
        checkFileSystem(g.getIdLong() , g.getDefaultChannel() );
	}

    public static List<Long> getWhitelisted() 
    {
        String path = AlloyUtil.ALLOY_PATH + "res\\perms\\white.listed";
        String[] sArr = FileReader.read(path);
        List<Long> list = new ArrayList<>();
        for (String id : sArr)
        {
            try {
                list.add(Long.parseLong(id));
            } catch (NumberFormatException e) {}
        }
        return list;
    }
    
    public static List<Long> getBlacklisted() 
    {
        String path = AlloyUtil.ALLOY_PATH + "res\\perms\\black.listed";
        String[] sArr = FileReader.read(path);
        List<Long> list = new ArrayList<>();
        for (String id : sArr)
        {
            try {
                list.add(Long.parseLong(id));
            } catch (NumberFormatException e) {}
        }
        return list;
	}

    public static long parseID(String file) 
    {
        String name = file;
        int numSlash = StringUtil.getNumCharsInString(name , "\\");
        for (int i = 0; i < numSlash ; i++) 
        {
            int index = name.indexOf(AlloyUtil.SUB);
            name = name.substring(index + 1);
        }
        return Long.parseLong(name); 
    }

    public static Member getMember( Guild g, String ping ) 
    {
        ping = ping.replace("<@!", "" );
        ping = ping.replace("<@" , "" );
        ping = ping.replace(">"  , "" );

        return g.getMemberById(ping);
	}

    public static Player loadPlayer(Guild g, Member m) 
    {
        PlayerLoaderText plt = new PlayerLoaderText();
        String path = getGuildPath(g) + USER_FOLDER + SUB + m.getId();
        Player p = plt.load(path);
        return p;
	}

    public static Guild getGuild(Server server, JDA jda) 
    {
        long id = server.getId();
        Guild g = jda.getGuildById(id);
        return g;
	}

    public static List<Player> loadAllPlayers(Guild g) 
    {
		PlayerLoaderText plt = new PlayerLoaderText();
        String path = getGuildPath(g) + USER_FOLDER + SUB;
        return plt.loadALl(path);
	}

    public static String getMemberPath(Member target) 
    {
        String out = getGuildPath(target.getGuild());
        out += USER_FOLDER + SUB;
        out += target.getId();
		return out;
	}

    public static Player loadPlayer(Member m) 
    {
		return loadPlayer(m.getGuild() , m);
	}

    public static List<String> loadWorkOptions(Server s) 
    {
        List<String> workOptions = new ArrayList<String>();
        String path = s.getPath();
        path += SETTINGS_FOLDER + SUB + WORK_OPTIONS_FILE;
        String[] arr = FileReader.read(path);
        for (String string : arr) 
            workOptions.add(string);
        return workOptions;

	}
}
