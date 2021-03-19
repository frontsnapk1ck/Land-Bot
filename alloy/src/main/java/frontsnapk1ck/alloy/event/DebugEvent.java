package frontsnapk1ck.alloy.event;

import frontsnapk1ck.utility.logger.Level;

public class DebugEvent {

    private Level level;
    private Throwable error;
    private String message;
    private String className;
    private Thread thread;
    
    private final long time;

    public DebugEvent(String className, String message, Throwable error, Level level, Thread t) 
    {
        this.className = className;
        this.message = message;
        this.error = error;
        this.level = level;
        this.thread = t;

        this.time = System.currentTimeMillis();
    }
    
    public String getClassName()
    {
        return className;
    }

    public Throwable getError()
    {
        return error;
    }

    public Level getLevel()
    {
        return level;
    }

    public String getMessage()
    {
        return message;
    }

    public long getTime() 
    {
        return time;
    }

    public Thread getThread()
    {
        return thread;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public void setError(Throwable error)
    {
        this.error = error;
    }

    public void setLevel(Level level)
    {
        this.level = level;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setThread(Thread thread)
    {
        this.thread = thread;
    }
    
    public boolean hasMessage()
    {
        return this.message != null;
    }
    
    public boolean hasError()
    {
        return this.error != null;
    }
    
    public boolean hasClassName()
    {
        return this.className != null;
    }
    
    public boolean hasLevel()
    {
        return this.level != null;
    }

    public boolean hasThread()
    {
        return this.thread != null;
    }
    
}
