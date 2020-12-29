package alloy.command.console;

import java.util.List;

import alloy.command.util.AbstractConsoleCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public class InviteCommand extends AbstractConsoleCommand {

    @Override
    public void execute(List<String> args , JDA jda) 
    {
        List<Guild> guilds = jda.getGuilds();
        for (Guild g : guilds) 
        {
            String code = "CANNOT MAKE INVITE";
            try {
                code = g.getDefaultChannel().createInvite().complete().getCode();
            } catch (Exception e) {}
            System.out.println(g.getName() + "\t\tdiscord.gg/" + code);
        }
    }
}
