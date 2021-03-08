package alloy.handler.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alloy.gameobjects.player.Building;
import alloy.gameobjects.player.Player;
import alloy.utility.discord.AlloyUtil;
import net.dv8tion.jda.api.entities.Member;

public class MeHandler {

    public static Map<Building, Integer> getOwned(Member m) 
    {
        Map<Building , Integer > out = new HashMap<Building , Integer>();
        Player p = AlloyUtil.loadPlayer(m);
        Map<String, List<Building>> owned = p.getOwned();
        List<String> keys = p.getTypes();

        for (String s : keys) 
        {
            List<Building> bs = owned.get(s);
            if (bs.size() != 0) 
            {
                Building b = bs.get(0);
                out.put(b, bs.size());
            }
        }
        
        return out;
	}
    
}
