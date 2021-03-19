package frontsnapk1ck.alloy.command.info;

import frontsnapk1ck.alloy.command.util.AbstractCooldownCommand;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.disterface.util.template.Template;
import frontsnapk1ck.alloy.templates.Templates;
import net.dv8tion.jda.api.entities.TextChannel;
import frontsnapk1ck.utility.time.TimeUtil;

public class UptimeCommand extends AbstractCooldownCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        
        long startupTime = Alloy.getStartupTimeStamp();

        String relativeTime = TimeUtil.getRelativeTime(startupTime);

        Template t = Templates.uptime(relativeTime);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom(getClass());
        bot.send(sm);
    }

}
