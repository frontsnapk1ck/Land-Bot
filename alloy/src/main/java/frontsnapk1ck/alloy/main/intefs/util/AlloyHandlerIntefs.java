package frontsnapk1ck.alloy.main.intefs.util;

import java.lang.Thread.UncaughtExceptionHandler;

import frontsnapk1ck.alloy.main.intefs.handler.AlloyHandler;
import frontsnapk1ck.alloy.main.intefs.handler.ConsoleHandler;
import frontsnapk1ck.alloy.main.intefs.handler.CooldownHandler;

public interface AlloyHandlerIntefs extends ConsoleHandler, AlloyHandler, CooldownHandler, UncaughtExceptionHandler {
    
}
