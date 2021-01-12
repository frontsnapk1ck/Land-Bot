package alloy.handler;

import java.util.ArrayList;
import java.util.List;

import alloy.gameobjects.Server;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.DisUtil;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import alloy.utility.event.SpamFinishEvent;
import alloy.utility.event.SpamFinishListener;
import alloy.utility.job.jobs.SpamRunnable;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class SpamHandler implements SpamFinishListener {

    private static List<SpamRunnable> spams;

    static {
        configure();
    }

    public static SpamRunnable makeRunnable(Guild guild, String[] args, User author) {
        Long num = getRandomNumber();
        Server s = AlloyUtil.loadServer(guild);
        Long channelID = s.getSpamChannel();
        TextChannel c = DisUtil.findChannel(guild, channelID);

        if (validCommand(args)) {
            int reps = getReps(guild, author, args);
            String message = buildMessage(args);

            SpamRunnable r = new SpamRunnable(reps, message, c, num);
            r.addListener(new SpamHandler());
            SpamHandler.spams.add(r);

            return r;
        }
        return null;
    }

    private static void configure() {
        spams = new ArrayList<SpamRunnable>();
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

    private static Long getRandomNumber() {
        boolean valid = false;
        Long l = 0l;

        while (!valid) {
            l = (long) (Math.random() * 100000000000l);
            if (spams.size() == 0)
                valid = true;
            for (SpamRunnable r : spams)
                valid = (r.getIDLong() == l) ? false : true;
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

    @Override
    public void onSpamFinishEvent(SpamFinishEvent e) {
        SpamHandler.spams.remove(e.getRunnable());
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

    public static boolean stopSpam(Long id) {
        SpamRunnable toRm = null;
        for (SpamRunnable r : spams) {
            if (r.getIDLong().equals(id))
                toRm = r;
        }

        if (toRm != null) {
            toRm.stop();
            spams.remove(toRm);
        }

        return toRm != null;
    }

}
