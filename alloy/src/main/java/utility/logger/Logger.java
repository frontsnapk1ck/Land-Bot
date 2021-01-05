package utility.logger;

import static utility.logger.Level.DEBUG;
import static utility.logger.Level.ERROR;
import static utility.logger.Level.WARN;
import static utility.logger.Level.INFO;

import org.slf4j.LoggerFactory;

public class Logger {

    private org.slf4j.Logger logger;

    public void warn(String className, String error) {
        onRecieve(className, error, WARN);
        logger = LoggerFactory.getLogger(className);
        logger.warn(error);
        logger = null;
    }

    public void error(String className, Throwable e) {
        onRecieve(className, e, ERROR);
        logger = LoggerFactory.getLogger(className);
        logger.error(e.getMessage(), e.getCause());
        e.printStackTrace();
        logger = null;
    }

    public void debug(String className, String message) {
        onRecieve(className, message, DEBUG);
        logger = LoggerFactory.getLogger(className);
        logger.debug(message);
        logger = null;
    }

    public void debug(String className, Exception e) {
        onRecieve(className, e, DEBUG);
        logger = LoggerFactory.getLogger(className);
        logger.debug(e.getMessage() , e);
        logger = null;
	}

    public void info(String className, String message) 
    {
        onRecieve(className , message , INFO);
        logger = LoggerFactory.getLogger(className);
        logger.info(message);
        logger = null;
    }
    
    private void onRecieve(String className, String message, Level level)
    {
        Thread t = Thread.currentThread();
        onRecieve(className, message, null , level , t);
    }

    private void onRecieve(String className, Throwable error, Level level) 
    {
        Thread t = Thread.currentThread();
        onRecieve(className, null , error , level , t);
    }
    
    protected void onRecieve(String className, String message, Throwable error , Level level, Thread t) 
    {
        // nothing to do yet
    }
}
