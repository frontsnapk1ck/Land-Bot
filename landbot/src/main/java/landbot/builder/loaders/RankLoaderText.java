package landbot.builder.loaders;

import java.util.ArrayList;
import java.util.List;

import landbot.builder.DataLoader;
import landbot.gameobjects.player.Rank;
import landbot.io.FileReader;

public class RankLoaderText extends DataLoader<Rank, String> {

    @Override
    public List<Rank> loadALl(String file) 
    {
        List<Rank> ranks = new ArrayList<Rank>();
        String[] arr = FileReader.read(file);
        String[][] rankArray = configureRankArray(arr); 

        for (String[] s : rankArray) 
            ranks.add(load(s));    

        return ranks;
    }

    private Rank load(String[] s) 
    {

        String levelString = s[0];
        String neededXPString = s[1];
        String totalXPString = s[2];

        int level =    Integer.parseInt(levelString);
        int xp = Integer.parseInt(neededXPString);
        int totalXP =  Integer.parseInt(totalXPString);

        Rank r = new Rank(level, xp, totalXP);

        return r;
    }

    private String[][] configureRankArray(String[] args) 
    {
        String[][] sArgs = new String[args.length][];

        int i = 0;
        for (String s : args)
        {
            sArgs[i] = s.split(":");
            i++;
        }
        
        return sArgs;
    }
    
}
