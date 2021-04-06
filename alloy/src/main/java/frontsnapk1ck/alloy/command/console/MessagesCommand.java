package frontsnapk1ck.alloy.command.console;

import java.math.BigInteger;

import frontsnapk1ck.alloy.command.util.AbstractConsoleCommand;
import frontsnapk1ck.alloy.input.console.ConsoleInputData;
import frontsnapk1ck.alloy.main.Alloy;

public class MessagesCommand extends AbstractConsoleCommand {

    @Override
    public void execute(ConsoleInputData data) 
    {
        Alloy bot = (Alloy)data.getQueue();
        BigInteger count = bot.getData().getMessages();
        System.out.println("Messages: " + count);
        System.out.println();
    }
    
}
