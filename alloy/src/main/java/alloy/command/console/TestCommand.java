package alloy.command.console;

import alloy.command.util.AbstractConsoleCommand;
import alloy.input.console.ConsoleInputData;
import alloy.main.Alloy;

public class TestCommand extends AbstractConsoleCommand {

    @Override
    public void execute(ConsoleInputData data) 
    {
        Alloy.LOGGER.info("TestCommand", "the test worked");
    }
    
}
