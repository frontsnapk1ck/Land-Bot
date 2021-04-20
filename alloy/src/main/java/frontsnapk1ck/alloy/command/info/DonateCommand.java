package frontsnapk1ck.alloy.command.info;

import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import frontsnapk1ck.alloy.templates.Templates;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class DonateCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        User author = data.getUser();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        AlloyTemplate t = Templates.donate(author);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }
    
}
