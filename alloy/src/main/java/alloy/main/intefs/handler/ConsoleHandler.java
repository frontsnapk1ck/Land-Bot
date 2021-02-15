package alloy.main.intefs.handler;

import alloy.input.console.ConsoleInput;
import net.dv8tion.jda.api.JDA;

public interface ConsoleHandler {

    public void handleConsoleMessage( ConsoleInput in );

	public JDA getJDA();
    
}
