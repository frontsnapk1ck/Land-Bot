package frontsnapk1ck.alloy.command.console;

import java.util.List;

import frontsnapk1ck.alloy.command.util.AbstractConsoleCommand;
import frontsnapk1ck.alloy.input.console.ConsoleInputData;
import frontsnapk1ck.utility.StringUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public class GuildsCommand extends AbstractConsoleCommand {

    @Override
    public void execute(ConsoleInputData data) 
    {
        List<String> args = data.getArgs();
        if (args.size() == 1)
            showAll(data);
    }

    private void showAll(ConsoleInputData data) 
    {
        JDA jda = data.getJda();
        List<Guild> guilds = jda.getGuilds();
        String[][] tableData = new String[guilds.size()][4];
        
        int i = 0;
        for (Guild g : guilds) 
        {
            tableData[i][0] = "" + (i+1);
            tableData[i][1] = g.getOwner().getUser().getAsTag();
            tableData[i][2] = g.getId();
            tableData[i][3] = g.getName();
            i++;
        }
        String out = StringUtil.makeTable(tableData, new String[]{"~~num~~" , "~~owner~~" , "~~id~~" , "~~name~~"});
        System.out.println(out);
    }
    
}
