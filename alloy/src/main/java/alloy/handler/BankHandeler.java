package alloy.handler;

import alloy.main.Alloy;
import alloy.utility.discord.DisUtil;
import alloy.utility.error.InvalidUserFormat;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class BankHandeler {

    public static final int MINIUM_BALACE = 100;
    public static final int INVALID_FORMAT = -2143201685;

    public static boolean isSend(Message msg) {
        return msg.getContentRaw().toLowerCase().contains("send");
    }

    public static boolean hasMessage(String[] args) 
    {
        return args.length > 2;
    }

    public static String getMessage(String[] args) 
    {
        String[] message = new String[args.length - 2];
        String out = "";

        int i = 0;
        for (String s : args) 
        {
            boolean v = getTargetUser(s) != null && 
                        getAmount(s) != BankHandeler.INVALID_FORMAT;
            if (v)
            {
                message[i] = s;
            }
        }

        for (String string : message) 
            out += string;

        return out;
    }

    public static User getTargetUser(String[] args) {
        for (String s : args) 
        {
            User u = getTargetUser(s);
            if (u != null)
                return u;
        }
        return null;
    }
    
    public static User getTargetUser(String s)
    {
        try 
        {
            User u = DisUtil.parseUser(s);
            return u;
        } 

        catch (InvalidUserFormat e) 
        {
            Alloy.LOGGER.debug("BankHandeler", e);
        }
        return null;
    }

    public static int getAmount(String[] args) 
    {
        for (String s : args) 
        {
            int num = getAmount(s);
            if (num != BankHandeler.INVALID_FORMAT)
                return num;
        }
        return BankHandeler.INVALID_FORMAT;
        
    }
    
    public static int getAmount(String s)
    {
        try 
        {
            int num = Integer.parseInt(s);
            return num;
        } 
        catch (NumberFormatException e) 
        {
            return BankHandeler.INVALID_FORMAT;
        }
    }
    
}
