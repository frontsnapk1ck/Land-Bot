package alloy.handler.command.config;

import alloy.gameobjects.Server;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import disterface.util.template.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import utility.Util;

public class StartingBalanceHandler {

    public static void viewStartingBalance(AlloyInputData data) 
    {
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Guild g = data.getGuild();

        Server s = AlloyUtil.loadServer(g);

        Template t = Templates.viewStartingBalance(s.getStartingBalance());
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(StartingBalanceHandler.class);
        sm.setMessage(t.getEmbed());
        bot.send(sm);  
	}

    public static void changeStartingBalance(AlloyInputData data, String balS) 
    {
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Guild g = data.getGuild();

        Server s = AlloyUtil.loadServer(g);

        try 
        {
            int bal = Util.parseInt(balS);
            s.changeStartingBalance(bal);
        }
        catch (NumberFormatException e)
        {
            Template t = Templates.invalidNumberFormat(balS);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(StartingBalanceHandler.class);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
        
	}
    
}
