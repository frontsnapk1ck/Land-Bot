package alloy.input.console;

import java.lang.Thread.UncaughtExceptionHandler;

import alloy.main.handler.ConsoleHandler;
import alloy.utility.runnable.InputRunnable;
import input.device.DeviceListener;
import net.dv8tion.jda.api.JDA;

public class Console implements DeviceListener  {

    private InputRunnable inputRunnable = new InputRunnable();

    private ConsoleHandler consoleHandler;
    private UncaughtExceptionHandler exceptionHandler;
    
    private Thread t;

    public Console() 
    {
        this.inputRunnable.setListener( this );
        t = new Thread( inputRunnable , "Alloy Console Input Listener" );
        t.setDaemon( false );
        t.start();
    }

    public ConsoleHandler getConsoleHandler() {
        return consoleHandler;
    }

    public UncaughtExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setHandler(ConsoleHandler consoleHandler) {
        this.consoleHandler = consoleHandler;
    }

    public void setHandler(UncaughtExceptionHandler exceptionHandler) 
    {
        this.exceptionHandler = exceptionHandler;
        t.setUncaughtExceptionHandler(exceptionHandler);
    }

    public boolean hasHandler()
    {
        return this.consoleHandler != null;
    }

    @Override
    public void onInput(String trigger) 
    {
        JDA jda = this.consoleHandler.getJDA();
        ConsoleInput input = new ConsoleInput( "CONSOLE" , trigger , "what the user typed" , jda );
        consoleHandler.handleConsoleMessage(input);
    }
    
}
