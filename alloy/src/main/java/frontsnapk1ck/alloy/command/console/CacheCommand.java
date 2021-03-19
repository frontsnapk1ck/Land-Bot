package frontsnapk1ck.alloy.command.console;

import java.util.List;
import java.util.Map;
import java.util.Set;

import frontsnapk1ck.alloy.command.util.AbstractConsoleCommand;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.gameobjects.player.Player;
import frontsnapk1ck.alloy.input.console.ConsoleInputData;
import frontsnapk1ck.alloy.utility.AlloyCache;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.utility.StringUtil;
import frontsnapk1ck.utility.cache.Cacheable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;

public class CacheCommand extends AbstractConsoleCommand {

    @Override
    public void execute(ConsoleInputData data) 
    {
        List<String> args = data.getArgs();

        if (args.size() == 1)
            showCache(data);
        else if (args.get(1).equalsIgnoreCase("clear"))
            clearCache(data);
    }

    private void showCache(ConsoleInputData data) 
    {
        AlloyCache cache = AlloyUtil.getCache();
        Map<String, Cacheable<?>> cacheable = cache.getCache();
        
        Set<String> set = cacheable.keySet();

        String[][] tableData = new String[set.size()][3];

        int i = 0;
        for (String string : set) 
        {
            tableData[i][0] = loadInfo(cacheable.get(string) , data);
            tableData[i][1] = cacheable.get(string).getClass().getSimpleName();
            tableData[i][2] = string;

            i++;
        }

        String[] headers = { "~~Info~~" , "~~Type~~" , "~~Path~~"};
        System.out.println(StringUtil.makeTable(tableData , headers));
    }

    private void clearCache(ConsoleInputData data) 
    {
        AlloyCache cache = AlloyUtil.getCache();
        cache.clear();
    }

    private String loadInfo(Cacheable<?> cacheable, ConsoleInputData data) 
    {
        if (cacheable.getClass() == Player.class)
            return player( (Player) cacheable.getData() , data.getJda() );
        if (cacheable.getClass() == Server.class)
            return server( (Server) cacheable.getData() , data.getJda() );

        return "No Info...";
    }

    private String server(Server server, JDA jda) 
    {
        return AlloyUtil.getGuild(server, jda).getName();
    }

    private String player(Player p, JDA jda) 
    {
        Member m = AlloyUtil.getMember(p , jda);
        try {
            return m.getUser().getAsTag();
        } catch (Exception e) 
        {
            return "null";
        }
    }
    
}
