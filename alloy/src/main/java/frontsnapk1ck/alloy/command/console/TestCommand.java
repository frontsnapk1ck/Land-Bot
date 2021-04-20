package frontsnapk1ck.alloy.command.console;

import frontsnapk1ck.alloy.command.util.AbstractConsoleCommand;
import frontsnapk1ck.alloy.input.console.ConsoleInputData;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.disterface.MessageData;
import frontsnapk1ck.disterface.MessageData.Destination;
import frontsnapk1ck.disterface.util.template.Template;

public class TestCommand extends AbstractConsoleCommand {

    @Override
    public void execute(ConsoleInputData data) 
    {
        Alloy.LOGGER.getDisInterface().clientSend(new MessageData(new Template("Test", "this is a test"), Destination.DEBUG));
        // DelayJob<String[]> s = new DelayJob<String[]>( strings -> {
        //     for (String string : strings)
        //         System.out.println(string);
        // }, new String[]{"test 1000" , "test"});
        // s.execute();
    }
    
}
