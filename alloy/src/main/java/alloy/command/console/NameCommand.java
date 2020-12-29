package alloy.command.console;

import java.util.List;

import alloy.command.util.AbstractConsoleCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class NameCommand extends AbstractConsoleCommand {

    @Override
    public void execute(List<String> args , JDA jda) 
    {
        if (args.size() == 2)
            gName(jda , args);
        if (args.size() == 3)
            mName(jda , args);
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

    private void mName(JDA jda, List<String> args) 
    {
        String gid = args.get(1);
        String mid = args.get(2);
        Guild g = jda.getGuildById(gid);
        if (g == null)
            return;
        Member m = g.getMemberById(mid);
        System.err.println(m.getUser().getAsTag());
    }
}
