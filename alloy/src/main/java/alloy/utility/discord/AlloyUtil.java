package alloy.utility.discord;

import java.util.ArrayList;
import java.util.List;

import alloy.builder.loaders.BuildingLoaderText;
import alloy.builder.loaders.PlayerLoaderText;
import alloy.builder.loaders.RankLoaderText;
import alloy.builder.loaders.ServerLoaderText;
import alloy.gameobjects.Server;
import alloy.gameobjects.player.Building;
import alloy.gameobjects.player.Player;
import alloy.gameobjects.player.Rank;
import alloy.utility.discord.paths.AlloyExtentions;
import alloy.utility.discord.paths.AlloyImages;
import alloy.utility.discord.paths.AlloyPaths;
import io.FileReader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import utility.StringUtil;

public class AlloyUtil implements AlloyPaths, AlloyExtentions, AlloyImages {

    public static List<Building> loadBuildings(Guild g) {
        BuildingLoaderText blt = new BuildingLoaderText();
        String bPath = getGuildPath(g) + "\\settings\\buildings.txt";
        List<Building> buildings = blt.loadALl(bPath);
        return buildings;
    }

    public static Server loadServer(Guild guild) {
        ServerLoaderText slt = new ServerLoaderText();
        return slt.load(getGuildPath(guild));
    }

    public static String getGuildPath(Guild guild) {
        return AlloyUtil.SERVERS_PATH + AlloyUtil.SUB + guild.getId();
    }

    public static String getPlayerPath(Guild guild, Long id) {
        return getGuildPath(guild) + "\\users\\" + id;
    }

    public static Member getMember(Guild g, Player p) {
        long id = p.getId();
        User u = User.fromId(id);
        return g.getMember(u);
    }

    public static List<Long> getWhitelisted() {
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

    public static List<Long> getBlacklisted() {
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

    public static long parseID(String file) {
        String name = file;
        int numSlash = StringUtil.getNumCharsInString(name, "\\");
        for (int i = 0; i < numSlash; i++) {
            int index = name.indexOf(AlloyUtil.SUB);
            name = name.substring(index + 1);
        }
        return Long.parseLong(name);
    }

    public static Member getMember(Guild g, String ping) {
        ping = ping.replace("<@!", "");
        ping = ping.replace("<@", "");
        ping = ping.replace(">", "");

        return g.getMemberById(ping);
    }

    public static Player loadPlayer(Guild g, Member m) {
        PlayerLoaderText plt = new PlayerLoaderText();
        String path = getGuildPath(g) + USER_FOLDER + SUB + m.getId();
        Player p = plt.load(path);
        return p;
    }

    public static Guild getGuild(Server server, JDA jda) {
        long id = server.getId();
        Guild g = jda.getGuildById(id);
        return g;
    }

    public static List<Player> loadAllPlayers(Guild g) {
        PlayerLoaderText plt = new PlayerLoaderText();
        String path = getGuildPath(g) + USER_FOLDER + SUB;
        return plt.loadALl(path);
    }

    public static String getMemberPath(Member target) {
        String out = getGuildPath(target.getGuild());
        out += USER_FOLDER + SUB;
        out += target.getId();
        return out;
    }

    public static Player loadPlayer(Member m) {
        return loadPlayer(m.getGuild(), m);
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

    public static List<Rank> loadAllRanks(Guild g) 
    {
        RankLoaderText rlt = new RankLoaderText();
        return rlt.loadALl(GLOBAL_RANK_PATH);
	}
}
