package alloy.command.fun;

import alloy.command.util.AbstractCommand;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.perm.DisPerm;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import utility.StringUtil;

public class LinkCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() 
    {
        return DisPerm.ADMINISTRATOR;
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        String[] args = AlloyInputUtil.getArgs(data);
        Message msg = data.getMessageActual();
        
        if (args.length < 2)
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("CooldownCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
        String link = args[0];
        String text = StringUtil.joinStrings(args,1);
        Template t = Templates.linkEmbed(link,text);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("CooldownCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        
        msg.delete().queue();

    }
    
}
