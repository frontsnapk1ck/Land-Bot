package botcord.util.event;

import java.util.ArrayList;
import java.util.List;

import alloy.event.DebugEvent;
import alloy.event.DebugListener;
import utility.logger.Level;
import utility.logger.Logger;

public class BotCordLogger extends Logger {
    
    private List<DebugListener> listeners;
    private List<DebugEvent> queue;

    public BotCordLogger() 
    {
        this.queue = new ArrayList<DebugEvent>();
        this.listeners = new ArrayList<DebugListener>();
    }

    @Override
    protected void onReceive(String className, String message, Throwable error, Level level, Thread t) 
    {
        DebugEvent e = new DebugEvent(className, message, error, level , t);
        try 
        {
            for (DebugListener l : listeners)
                l.onReceive(e);
        } catch (NullPointerException eNull) 
        {
            this.queue.add(e);
        }
    }

    public void addListener(DebugListener listener)
    {
        this.listeners.add(listener);
    }
    
    public boolean rmListener(DebugListener listener)
    {
        return this.listeners.remove(listener);
    }

    public void setListeners(List<DebugListener> listeners) 
    {
        this.listeners = listeners;
        checkQueue();
    }


    private void checkQueue() 
    {
        if (this.listeners == null)
            return;
        
        for (DebugEvent e : queue) 
        {
            for (DebugListener l : listeners) 
                l.onReceive(e);
        }
    }

}
