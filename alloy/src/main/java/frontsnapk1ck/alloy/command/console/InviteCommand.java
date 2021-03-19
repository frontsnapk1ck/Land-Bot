package frontsnapk1ck.alloy.command.console;

import java.util.List;

import frontsnapk1ck.alloy.command.util.AbstractConsoleCommand;
import frontsnapk1ck.alloy.input.console.ConsoleInputData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import frontsnapk1ck.utility.StringUtil;

public class InviteCommand extends AbstractConsoleCommand {

    @Override
    public void execute(ConsoleInputData data) 
    {
        JDA jda = data.getJda();

        List<Guild> guilds = jda.getGuilds();
        String[][] out = new String[guilds.size()][2];

        int i = 0;
        for (Guild g : guilds) 
        {
            String code = "CANNOT MAKE INVITE";
            try {
                code = g.getDefaultChannel().createInvite().complete().getCode();
            } catch (Exception e) {}
            out[i] = new String[]{ g.getName() ,"discord.gg/" + code };

            i++;
        }
        String[] headers = new String[] {"~~~guild~~~" , "~~~code~~~"};
        String table = StringUtil.makeTable(out, headers);
        System.out.println(table);
    }
}
