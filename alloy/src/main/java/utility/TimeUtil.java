package utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeUtil {

    public static final int SECOND_MILLIS = 1000;
    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public static final int WEEK_MILLIS = 7 * DAY_MILLIS;

    private static final SimpleDateFormat timestamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private static final SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy/MM/dd");
    private static final Map<Character, Long> TIME_SYMBOLS = new HashMap<>();

    static {
        TIME_SYMBOLS.put('w', 604800000L);
        TIME_SYMBOLS.put('d', 86400000L);
        TIME_SYMBOLS.put('h', 3600000L);
        TIME_SYMBOLS.put('m', 60000L);
        TIME_SYMBOLS.put('s', 1000L);
    }

    public static String getTimestampFormat(long time) 
    {
        try {
            return timestamp.format(new Date(time));
        } catch (Exception e) {
            return "cant figure out (" + time + ")";
        }
    }

    public static String formatYMD(Date date) 
    {
        return ymdFormat.format(date);
    }

    public static long toMillis(String s) 
    {
        s = s.toLowerCase();
        long val = 0;
        String working = "";
        for (int i = 0; i < s.length(); i++) 
        {
            if (Character.isDigit(s.charAt(i)))
                working += s.charAt(i);
            else if (   TIME_SYMBOLS.containsKey(s.charAt(i)) &&  working.length() > 0)
                    val += Util.parseInt(working.toString(), 0) * TIME_SYMBOLS.get(s.charAt(i));
        }
        if (working.length() != 0)
            val += Util.parseInt(working.toString(), 0);
        return val;
    }

    public static String getRealitiveTime( long time ) 
    {
        return getRealitiveTime(time , true);
    }

    public static String getRealitiveTime( long time , boolean shortText )
    {
        long usedTime = time;
        boolean future = false;
        String chronology = "ago";
        long now = System.currentTimeMillis();
        if (usedTime <= 0) {
            return "???";
        }
        long diff;
        if (usedTime > now) 
        {
            diff = usedTime - now;
            chronology = "from now";
            future = true;
        } 
        else
            diff = now - usedTime;

        if (diff < MINUTE_MILLIS)
            return (diff / SECOND_MILLIS) + (shortText ? "s" : " seconds " + chronology);
        else if (diff < 2 * MINUTE_MILLIS)
            return shortText ? "~1m" : "about a minute " + chronology;
        else if (diff < 50 * MINUTE_MILLIS)
            return diff / MINUTE_MILLIS + (shortText ? "m" : " minutes " + chronology);
        else if (diff < 90 * MINUTE_MILLIS)
            return shortText ? "~1h" : "about an hour " + chronology;
        else if (diff < 24 * HOUR_MILLIS)
            return diff / HOUR_MILLIS + (shortText ? "h" : " hour" + (diff / HOUR_MILLIS == 1 ? "" : "s") + " " + chronology);
        else if (diff < 48 * HOUR_MILLIS)
            return shortText ? "~1d" : future  ?  "about a day" : "yesterday";
        else if (diff < 14 * DAY_MILLIS || !shortText)
            return diff / DAY_MILLIS + (shortText ? "d" : " day" + (diff == 1 ? "" : "s") + " " + chronology);
        return ">2w";
    } 
}
