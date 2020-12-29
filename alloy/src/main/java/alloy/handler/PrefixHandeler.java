package alloy.handler;

import alloy.gameobjects.Server;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class PrefixHandeler {

    public static final int MAX_PREFIX_LENTH = 4;

    public static boolean changePrefix(Guild g, String prefix) 
    {
        Server s = AlloyUtil.loadServer(g);
        if (validPrefix(prefix))
            s.changePrefix(prefix);
        return validPrefix(prefix);
	}

    private static boolean validPrefix(String prefix) 
    {
        return prefix.length() <= MAX_PREFIX_LENTH;
    }

    public static void viewPrefix(TextChannel tc , Sendable bot , Guild g) 
    {
        Server s = AlloyUtil.loadServer(g);
        String prefix = s.getPrefix();

        Template t = Templates.prefixIs(prefix);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(tc);
        sm.setFrom("PrefixHandeler");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
	}


    
}
