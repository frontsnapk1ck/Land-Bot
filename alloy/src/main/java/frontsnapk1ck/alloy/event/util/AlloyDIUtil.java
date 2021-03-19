package frontsnapk1ck.alloy.event.util;

import frontsnapk1ck.disterface.MessageData.Destination;
import frontsnapk1ck.disterface.util.DIUtil;
import frontsnapk1ck.utility.logger.Level;

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
