package utility;

import org.slf4j.LoggerFactory;

public class Logger {

    private org.slf4j.Logger logger;

    public void warn(String className, String error) 
    {
        logger = LoggerFactory.getLogger(className);
        logger.warn(error);
        logger = null;
	}

    public void error(String className, Throwable e) 
    {
        logger = LoggerFactory.getLogger(className);
        logger.error(e.getMessage() , e.getCause());
        e.printStackTrace();
        logger = null;
	}

    public void debug(String className, String message) 
    {
        logger = LoggerFactory.getLogger(className);
        logger.debug(message);
        logger = null;
    }
    
    public void debug(String className, Exception e) 
    {
        logger = LoggerFactory.getLogger(className);
        logger.debug(e.getMessage() , e);
        logger = null;
	}

    public void info(String className, String message) 
    {
        logger = LoggerFactory.getLogger(className);
        logger.info(message);
        logger = null;
	}



    
    
}
