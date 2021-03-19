package frontsnapk1ck.alloy.command.console;

import java.util.List;
import java.util.function.Consumer;

import frontsnapk1ck.alloy.command.util.AbstractConsoleCommand;
import frontsnapk1ck.alloy.input.console.ConsoleInputData;
import frontsnapk1ck.alloy.utility.job.jobs.DelayJob;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import frontsnapk1ck.utility.StringUtil;

public class MembersCommand extends AbstractConsoleCommand {

    @Override
    public void execute(ConsoleInputData data) 
    {        
        Consumer<ConsoleInputData> con = new Consumer<ConsoleInputData>()
        {
            @Override
            public void accept(ConsoleInputData t) 
            {
                membersImp(t);
            }
        };
        DelayJob<ConsoleInputData> j = new DelayJob<ConsoleInputData>(con, data);
        data.getQueue().queue(j);
    }

    protected void membersImp(ConsoleInputData data) 
    {
        JDA jda = data.getJda();
     
        List<String> args = data.getArgs();
        if (args.size() < 2)
            return;

        Guild g = jda.getGuildById(args.get(1));
        List<Member> members = g.getMembers();
        String[][] arr = new String[members.size()][3];

        String[] headers = { "~~nick~~", "~~tag~~", "~~id~~", };

        for (int i = 0; i < arr.length; i++) {
            Member m = members.get(i);
            arr[i][0] = m.getEffectiveName();
            arr[i][1] = m.getUser().getAsTag();
            arr[i][2] = m.getId();
        }
        String table = StringUtil.makeTable(arr, headers);
        System.out.println(table);
    }
}
