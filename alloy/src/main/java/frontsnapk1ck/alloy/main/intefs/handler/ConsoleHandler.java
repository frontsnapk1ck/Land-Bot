package frontsnapk1ck.alloy.main.intefs.handler;

import frontsnapk1ck.alloy.input.console.ConsoleInput;
import net.dv8tion.jda.api.JDA;

public interface ConsoleHandler {

    public void handleConsoleMessage( ConsoleInput in );

	public JDA getJDA();
    
}
