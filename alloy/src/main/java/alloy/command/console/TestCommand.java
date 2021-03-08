package alloy.command.console;

import alloy.command.util.AbstractConsoleCommand;
import alloy.input.console.ConsoleInputData;
import alloy.utility.job.jobs.DelayJob;

public class TestCommand extends AbstractConsoleCommand {

    @Override
    public void execute(ConsoleInputData data) 
    {
        DelayJob<String[]> s = new DelayJob<String[]>(TestCommand::string, new String[]{"test 1000" , "test"});
        s.execute();
    }

    public static void string(String[] s) 
    {
        for (String string : s) 
            System.err.println(string);
    }
    
}
