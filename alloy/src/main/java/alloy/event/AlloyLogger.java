package alloy.event;

import java.util.ArrayList;
import java.util.List;

import utility.logger.Level;
import utility.logger.Logger;

public class AlloyLogger extends Logger {

    private List<DebugListener> listeners;
    private List<DebugEvent> queue;
    private DiscordInterface disInterface;

    public AlloyLogger() 
    {
        this.queue = new ArrayList<DebugEvent>();
        this.listeners = new ArrayList<DebugListener>();
    }

    @Override
    protected void onReceive(String className, String message, Throwable error, Level level, Thread t) 
    {
        DebugEvent e = new DebugEvent(className, message, error, level , t);
        for (DebugListener l : listeners)
            l.onReceive(e);
        if (this.queue == null)
            this.queue.add(e);
    }

    public void addListener(DebugListener listener)
    {
        if (listener instanceof DiscordInterface)
            this.disInterface = (DiscordInterface) listener;
        this.listeners.add(listener);
    }
    
    public boolean rmListener(DebugListener listener)
    {
        if (listener == this.disInterface)
            this.disInterface = null;
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

    public DiscordInterface getDisInterface() {
        return disInterface;
    }
    
}
