package frontsnapk1ck.alloy.command.util;

import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.alloy.input.console.ConsoleInputData;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.utility.discord.perm.AlloyPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import net.dv8tion.jda.api.JDA;

public abstract class AbstractConsoleCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() 
    {
        return AlloyPerm.CONSOLE;
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        String[] argArr = data.getMessage().split(" ");
        List<String> args = new ArrayList<String>();
        for (String string : argArr) 
            args.add(string);
        JDA jda = data.getJDA();
        ConsoleInputData dataC = new ConsoleInputData();
        dataC.setArgs(args);
        dataC.setJda(jda);
        dataC.setQueue(data.getQueue());
        dataC.setSendable(data.getSendable());
        dataC.setBot(data.getBot());
        
        execute(dataC);
    }

    public abstract void execute( ConsoleInputData data );
    
}
