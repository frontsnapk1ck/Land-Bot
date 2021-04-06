package frontsnapk1ck.alloy.command.console;

import frontsnapk1ck.alloy.command.util.AbstractConsoleCommand;
import frontsnapk1ck.alloy.input.console.ConsoleInputData;
import frontsnapk1ck.alloy.utility.job.jobs.DelayJob;

public class TestCommand extends AbstractConsoleCommand {

    @Override
    public void execute(ConsoleInputData data) 
    {
        DelayJob<String[]> s = new DelayJob<String[]>( strings -> {
            for (String string : strings)
                System.out.println(string);
        }, new String[]{"test 1000" , "test"});
        s.execute();
    }
    
}
