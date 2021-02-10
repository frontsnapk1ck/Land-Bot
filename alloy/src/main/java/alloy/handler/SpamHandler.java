package alloy.handler;

import java.util.ArrayList;
import java.util.List;

import alloy.gameobjects.Server;
import alloy.handler.util.SpamContainer;
import alloy.main.Queueable;
import alloy.main.Sendable;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.DisUtil;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class SpamHandler {

    private static List<SpamContainer> spams;

    static {
        configure();
    }

    public static SpamContainer makeRunnable(Guild guild, String[] args, User author, Sendable sendable, Queueable queue) 
    {
        Long num = getRandomNumber();
        Server s = AlloyUtil.loadServer(guild);
        Long channelID = s.getSpamChannel();
        TextChannel c = DisUtil.findChannel(guild, channelID);

        if (validCommand(args)) {
            int reps = getReps(guild, author, args);
            String message = buildMessage(args);

            SpamContainer container = new SpamContainer(reps, message, c, num , sendable , queue);
            container.setId(num);
            SpamHandler.spams.add(container);

            return container;
        }
        return null;
    }

    private static void configure() 
    {
        spams = new ArrayList<SpamContainer>();
    }

    public static boolean validCommand(String[] args) {
        boolean valid = false;
        valid = args.length >= 2;
        if (!valid)
            return false;

        try {
            int num = Integer.parseInt(args[0]);
            if (num < 1)
                return false;
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    private static Long getRandomNumber() 
    {
        boolean valid = false;
        Long l = 0l;

        while (!valid) 
        {
            l = (long) (Math.random() * 100000000000l);
            if (spams.size() == 0)
                valid = true;
            for (SpamContainer r : spams)
                valid = (r.getID() == l) ? false : true;
        }
        return l;
    }

    private static String buildMessage(String[] args) {
        String message = "";
        for (int i = 1; i < args.length; i++)
            message += args[i] + " ";

        message = message.replace("@everyone", "@ everyone");
        return message;
    }

    private static int getReps(Guild g, User author, String[] args) {
        int num = Integer.parseInt(args[0]);

        boolean five = !DisPermUtil.checkPermission(g.getMember(author), DisPerm.ADMINISTRATOR)
                && author.getIdLong() != 312743142828933130l;

        boolean sixty = !DisPermUtil.checkPermission(g.getMember(author), DisPerm.ADMINISTRATOR)
                && author.getIdLong() != 312743142828933130l;
        if (five)
            num = num > 5 ? 5 : num;
        else if (sixty)
            num = num > 60 ? 60 : num;
        return num;

    }

    public static boolean isStart(String[] args) {
        try {
            Integer.parseInt(args[0]);
            return args.length > 1;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isStop(String[] args) {
        try {
            Long.parseLong(args[1]);
            return args[0].equalsIgnoreCase("stop");
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean stopSpam(Long id) 
    {
        SpamContainer toRm = null;
        for (SpamContainer r : spams) 
        {
            if (r.getID().equals(id))
                toRm = r;
        }

        if (toRm != null) 
        {
            toRm.stop();
            spams.remove(toRm);
        }

        return toRm != null;
    }

}
