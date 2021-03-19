package frontsnapk1ck.alloy.input.console;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.alloy.main.intefs.handler.ConsoleHandler;
import frontsnapk1ck.alloy.utility.runnable.InputRunnable;
import frontsnapk1ck.input.device.DeviceListener;
import net.dv8tion.jda.api.JDA;

public class Console implements DeviceListener  {

    private InputRunnable inputRunnable = new InputRunnable();

    private ConsoleHandler consoleHandler;
    
    private Thread t;

    public Console() 
    {
        this.inputRunnable.setListener( this );
        t = new Thread( inputRunnable , "Alloy Console Input Listener" );
        t.setDaemon( false );
        t.start();
    }

    public ConsoleHandler getConsoleHandler()
    {
        return consoleHandler;
    }


    public void setHandler(ConsoleHandler consoleHandler, UncaughtExceptionHandler alloy)
    {
        this.consoleHandler = consoleHandler;
        Thread.setDefaultUncaughtExceptionHandler(alloy);
    }

    public boolean hasHandler()
    {
        return this.consoleHandler != null;
    }

    @Override
    public void onInput(String trigger) 
    {
        JDA jda = this.consoleHandler.getJDA();
        List<String> args = getArgs(trigger);

        ConsoleInputData data = new ConsoleInputData();
        data.setArgs(args);
        data.setJda(jda);

        ConsoleInput input = new ConsoleInput( "CONSOLE" , trigger , data );
        consoleHandler.handleConsoleMessage(input);
    }

    private List<String> getArgs(String trigger)
    {
        String[] args = trigger.split(" ");
        List<String> strings = new ArrayList<String>();
        for (String s : args)
            strings.add(s);
        
        return strings;
    }
    
}
