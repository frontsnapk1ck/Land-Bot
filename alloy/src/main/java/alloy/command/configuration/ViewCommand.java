package alloy.command.configuration;

import alloy.command.util.AbstractCommand;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class ViewCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (args.length != 1 )
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage() );
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("ViewCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args[0].equalsIgnoreCase("work"))
            work(data);
        else if (args[0].equalsIgnoreCase("building"))
            building(data);

    }

    private void work( AlloyInputData data )
    {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        Template t = Templates.workOptions(g);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("ViewCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);        
    }

    private void building( AlloyInputData data )
    {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        Template t = Templates.buildingsList(g);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("ViewCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);    }
    
}
