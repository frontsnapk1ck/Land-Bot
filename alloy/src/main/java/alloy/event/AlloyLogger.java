package alloy.event;

import java.util.ArrayList;
import java.util.List;

import utility.logger.Level;
import utility.logger.Logger;

public class AlloyLogger extends Logger {

    private DebugListener listener;
    private List<DebugEvent> queue;

    public AlloyLogger() 
    {
        this.queue = new ArrayList<DebugEvent>();
    }

    @Override
    protected void onReceive(String className, String message, Throwable error, Level level, Thread t) 
    {
        DebugEvent e = new DebugEvent(className, message, error, level , t);
        try {
            listener.onReceive(e);
        } catch (NullPointerException eNull) 
        {
            this.queue.add(e);
        }
    }

    public void setListener(DebugListener listener) 
    {
        this.listener = listener;
        checkQueue();
    }

    private void checkQueue() 
    {
        if (this.listener == null)
            return;
        
        for (DebugEvent e : queue) 
            this.listener.onReceive(e);
    }
    
}
