package frontsnapk1ck.alloy.utility.discord.paths;

import java.io.File;

public interface AlloyFolderConstants {

    public static final String ALLOY_PATH                       = findAlloyPath();
    public static final String SERVERS_PATH                     = ALLOY_PATH + "res/servers";
    
    private static String findAlloyPath() 
    {
        String local = "H:/Coding/Discord Bots/Alloy/Alloy/alloy/";
        if (new File( local ).exists())
            return local;
        
        String server = "/home/shawn/Alloy/";
        if (new File( server ).exists())
            return server;
        
        throw new Error("Paths invalid");
    }
    
}
