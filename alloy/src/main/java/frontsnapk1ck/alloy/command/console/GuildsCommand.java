package frontsnapk1ck.alloy.command.console;

import java.util.List;

import frontsnapk1ck.alloy.command.util.AbstractConsoleCommand;
import frontsnapk1ck.alloy.input.console.ConsoleInputData;
import frontsnapk1ck.utility.StringUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Guild.Ban;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class GuildsCommand extends AbstractConsoleCommand {

    @Override
    public void execute(ConsoleInputData data) 
    {
        List<String> args = data.getArgs();
        if (args.size() == 1)
        {
            showAll(data);
            return;
        }
        
        try 
        {
            Guild g = data.getJda().getGuildById(args.get(1));
            if (args.get(2).equalsIgnoreCase("bans"))
                showBans(g , data);
        
            if (args.get(2).equalsIgnoreCase("ban"))
                ban(g,data);
            if (args.get(2).equalsIgnoreCase("unban"))
                unban(g,data);

        
            if(args.get(2).equalsIgnoreCase("leave"))
                leave(g);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("fail");
        }
    }

    private void leave(Guild g) 
    {
        g.leave().complete();
    }

    private void unban(Guild g, ConsoleInputData data) 
    {
        try {
            User u = data.getJda().getUserById(data.getArgs().get(3));
            g.unban(data.getArgs().get(3)).complete();

            String name = "";
            if ( u != null)
                name = u.getAsTag();
            else
                name = data.getArgs().get(3);
        
            System.out.println("unbanned the user: " + name);
        }
        catch (Exception e) 
        {
            System.out.println("The User could not be unbanned with message: " + e.getMessage());
        }
        
    }

    private void ban(Guild g, ConsoleInputData data) 
    {
        try 
        {
            Member m = g.getMemberById(data.getArgs().get(3));
            g.ban(data.getArgs().get(3) , 0).complete();

            String name = "";
            if (m != null)
                name = m.getUser().getAsTag();
            else
                name = data.getArgs().get(3);
            
            System.out.println("banned the user: " + name);
        } catch (Exception e) 
        {
            System.out.println("The User could not be banned with message: " + e.getMessage());
        }
    }

    private void showBans(Guild g, ConsoleInputData data) 
    {
        List<Ban> bans = g.retrieveBanList().complete();
        String[][] dataOut = new String[bans.size()][4];
        
        int i = 0;
        for (Ban b : bans) 
        {
            dataOut[i][0] = "" + (i+1);
            dataOut[i][1] = b.getUser().getAsTag();
            dataOut[i][2] = b.getUser().getId();
            dataOut[i][3] = (b.getReason() == null || b.getReason().equalsIgnoreCase("")) ? "No reason provided" : b.getReason();
            i++;
        }
        String out = StringUtil.makeTable(dataOut, new String[]{"~~num~~" , "~~id~~" , "~~user~~" , "~~reason~~"});
        System.out.println(out);
    }

    private void showAll(ConsoleInputData data) 
    {
        JDA jda = data.getJda();
        List<Guild> guilds = jda.getGuilds();
        String[][] tableData = new String[guilds.size()][5];
        
        int i = 0;
        for (Guild g : guilds) 
        {
            tableData[i][0] = "" + (i+1);
            tableData[i][1] = "" + g.getMemberCount();
            tableData[i][2] = g.getOwner().getUser().getAsTag();
            tableData[i][3] = g.getId();
            tableData[i][4] = g.getName();
            i++;
        }
        String out = StringUtil.makeTable(tableData, new String[]{"~~num~~" , "~~members~~" ,  "~~owner~~" , "~~id~~" , "~~name~~"});
        System.out.println(out);
    }
    
}
