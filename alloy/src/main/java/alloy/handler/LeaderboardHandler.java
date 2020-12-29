package alloy.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import alloy.builder.loaders.RankLoaderText;
import alloy.gameobjects.player.Player;
import alloy.gameobjects.player.Rank;
import alloy.utility.discord.AlloyUtil;
import net.dv8tion.jda.api.entities.Guild;

public class LeaderboardHandler {

    public static final int MAX_LB_LENGTH = 10;

    public static List<String> loadLeaderboard(Guild g) 
    {
        List<Player> players = AlloyUtil.loadAllPlayers(g);
        List<String> positions = new ArrayList<String>();

        Collections.sort(players);

        for (Player player : players) 
            positions.add(getLBRank(player));
        
        return positions;
    }

    private static String getLBRank(Player player) 
    {
        int xp = player.getXP();

        RankLoaderText rlt = new RankLoaderText();
        List<Rank> stock = rlt.loadALl(AlloyUtil.GLOBAL_RANK_PATH);

        int level = findLevel(stock, xp);
        String progress = findProgess(xp, level, stock);

        return "<@!" + player.getId() + ">\nlevel: `" + level + "`\nxp: `" + progress + "`\n";
    }

    private static int findLevel(List<Rank> stock, int xp) {
        for (Rank rank : stock) {
            if (rank.getTotalXP() > xp)
                return rank.getLevel() - 1;

        }
        return 40;
    }

    private static String findProgess(int xp, int level, List<Rank> stock) 
    {
        int nextLevel = level < stock.size() ? level : stock.size() - 1;
        Rank rank = stock.get(level - 1);
        Rank nextRank = stock.get(nextLevel);
        int progress = xp - rank.getTotalXP();
        String progressCombined = "" + progress + "/" + nextRank.getXP();
        return progressCombined;

    }
    
}
