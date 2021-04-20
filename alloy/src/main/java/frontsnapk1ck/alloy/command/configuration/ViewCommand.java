package frontsnapk1ck.alloy.command.configuration;

import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import frontsnapk1ck.alloy.templates.Templates;
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
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage() );
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
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

        AlloyTemplate t = Templates.workOptions(g);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);        
    }

    private void building( AlloyInputData data )
    {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        AlloyTemplate t = Templates.buildingsList(g);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);    }
    
}
