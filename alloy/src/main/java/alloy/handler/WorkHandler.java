package alloy.handler;

import alloy.utility.discord.AlloyUtil;
import io.FileReader;
import io.Saver;
import net.dv8tion.jda.api.entities.Guild;
import utility.StringUtil;

public class WorkHandler {

    public static void addWorkOption(Guild g, String[] args) {
        String out = StringUtil.joinStrings(args, 1);

        String path = AlloyUtil.getGuildPath(g) + "\\settings\\work.options";
        Saver.saveAppend(path, out);
    }

    public static String removeWork(int rm, Guild g) throws IndexOutOfBoundsException {
        String path = AlloyUtil.getGuildPath(g) + "\\settings\\work.options";
        String[] options = FileReader.read(path);

        if (rm > 0 && rm <= options.length) {
            String s = options[rm - 1].toString();
            options[rm - 1] = null;
            String[] newOp = new String[options.length - 1];

            int i = 0;
            for (String st : options) {
                if (st != null) {
                    newOp[i] = st;
                    i++;
                }
            }
            Saver.saveOverwite(path, newOp);
            return s;
        } else
            throw new IndexOutOfBoundsException("" + rm + " is out of bounds for " + options.length);

    }

    public static void resetWork(Guild g) {
        String to = AlloyUtil.getGuildPath(g) + AlloyUtil.SETTINGS_FOLDER + AlloyUtil.SUB + AlloyUtil.WORK_OPTIONS_FILE;
        String from = AlloyUtil.GLOBAL_WORK_OPTIONS_PATH;

        Saver.copyFrom(from, to);
    }

}
