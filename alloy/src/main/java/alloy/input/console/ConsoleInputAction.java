package alloy.input.console;

import java.util.List;

import input.InputAction;
import net.dv8tion.jda.api.JDA;

public interface ConsoleInputAction extends InputAction {

    public void execute( List<String> args , JDA jda );

}
