package frontsnapk1ck.alloy.command.console;

import java.util.List;

import frontsnapk1ck.alloy.command.util.AbstractConsoleCommand;
import frontsnapk1ck.alloy.input.console.ConsoleInputData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class NameCommand extends AbstractConsoleCommand {

    @Override
    public void execute(ConsoleInputData data) 
    {
        JDA jda = data.getJda();
        List<String> args = data.getArgs();


        if (args.size() == 2)
            gName(jda , args);
        if (args.size() == 3 && args.get(1).equalsIgnoreCase("user"))
            user(jda , args);
        if (args.size() == 3 && args.get(1).equalsIgnoreCase("guild"))
            gName(jda , args);
    }

    private void user(JDA jda, List<String> args) 
    {
        try {
            User u = jda.getUserById(args.get(2));
            System.out.println(u.getAsTag());
        } catch (Exception ignored){
        }
    }

    private void gName(JDA jda, List<String> args) 
    {
        String id = args.get(1);
        if (id == null)
            return;
        Guild g = jda.getGuildById(id);
        if (g == null)
            return;
        System.out.println(g.getName());
    }
}
