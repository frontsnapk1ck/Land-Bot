package alloy.event.util;

import disterface.MessageData.Destination;
import disterface.util.DIUtil;
import utility.logger.Level;

public class AlloyDIUtil extends DIUtil {

    public static Destination parse(Level level) 
    {
        if (level.equals(Level.INFO))
            return Destination.INFO;
        if (level.equals(Level.WARN))
            return Destination.WARN;
        if (level.equals(Level.DEBUG))
            return Destination.DEBUG;
        if (level.equals(Level.ERROR))
            return Destination.ERROR;
        return null;
	}
    
}
