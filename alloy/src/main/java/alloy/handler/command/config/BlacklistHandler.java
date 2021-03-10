package alloy.handler.command.config;

import java.util.List;

import alloy.gameobjects.Server;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import disterface.util.template.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class BlacklistHandler {

    public static void view(Guild g, TextChannel channel, Sendable bot) 
    {
        Server s = AlloyUtil.loadServer(g);
        List<Long> blacklisted = s.getBlacklistedChannels();
        Template t;
        if ( blacklisted == null || blacklisted.size() == 0 )
            t = Templates.noBlacklistedChannels();
        else
            t = Templates.listBlackListedChannels(blacklisted);
        
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom(BlacklistHandler.class);
        bot.send( sm );
	}

    public static boolean isBlacklisted(Guild g, String channel) 
    {
        Server s = AlloyUtil.loadServer(g);
        channel = channel.replace("<#", "");
        channel = channel.replace(">", "");

        return s.getBlacklistedChannels().contains(Long.parseLong(channel));
	}

    public static void add(Guild g, String channel )
    {
        Server s = AlloyUtil.loadServer(g);
        channel = channel.replace("<#", "");
        channel = channel.replace(">", "");

        s.addBlacklistedChanel(Long.parseLong(channel));

	}

    public static boolean remove(Guild g, String channel) 
    {
        Server s = AlloyUtil.loadServer(g);
        channel = channel.replace("<#", "");
        channel = channel.replace(">", "");

        return s.removeBlackListedChannel(Long.parseLong(channel));
	}
    
}
