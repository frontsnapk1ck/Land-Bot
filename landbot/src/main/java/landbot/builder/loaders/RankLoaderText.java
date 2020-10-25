package landbot.builder.loaders;

import landbot.builder.DataLoader;
import landbot.io.FileReader;
import landbot.player.Rank;

public class RankLoaderText extends DataLoader<Rank, String> {

    @Override
    public Rank load(String file) 
    {
        String[] args = FileReader.read(file);
        String[][] rankArray = configureRankArray(args);

        Rank r = new Rank( file );
        
        return r;
    }

    private String[][] configureRankArray(String[] args) 
    {
        String[][] sArgs = new String[args.length][];

        int i = 0;
        for (String s : args)
            sArgs[i] = s.split(">");
        
        return sArgs;
    }

    private static String loadSetting(String s, String[][] accountArray) 
    {
        for (String[] strings : accountArray) 
        {
            if (strings[0].equalsIgnoreCase(s))
                return strings[1];
        }
        return null;
    }
    
}
