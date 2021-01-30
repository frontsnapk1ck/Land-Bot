package alloy.command.console;

import java.util.List;

import alloy.command.util.AbstractConsoleCommand;
import alloy.input.console.ConsoleInputData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import utility.StringUtil;

public class MembersCommand extends AbstractConsoleCommand {

    @Override
    public void execute(ConsoleInputData data) 
    {
        JDA jda = data.getJda();
        List<String> args = data.getArgs();

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
        System.err.println(table);
    }
}
