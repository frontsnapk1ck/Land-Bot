package alloy.command.info;

import alloy.command.util.AbstractCommand;
import alloy.input.discord.AlloyInputData;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class DonateCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        User author = data.getUser();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        Template t = Templates.donate(author);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("HelpCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }
    
}
