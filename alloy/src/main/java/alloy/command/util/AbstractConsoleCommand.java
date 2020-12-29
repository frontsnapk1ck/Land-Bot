package alloy.command.util;

import java.util.ArrayList;
import java.util.List;

import alloy.input.discord.AlloyInputData;
import alloy.utility.discord.perm.AlloyPerm;
import alloy.utility.discord.perm.DisPerm;
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
        execute(args , jda);
    }

    public abstract void execute( List<String> args , JDA jda );
    
}
