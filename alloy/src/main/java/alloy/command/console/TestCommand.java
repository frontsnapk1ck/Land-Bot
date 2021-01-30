package alloy.command.console;

import alloy.command.util.AbstractConsoleCommand;
import alloy.input.console.ConsoleInputData;

public class TestCommand extends AbstractConsoleCommand {

    @Override
    public void execute(ConsoleInputData data) 
    {
        throw new RuntimeException("you asked for it");
    }
    
}
