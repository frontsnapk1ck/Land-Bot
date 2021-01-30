package alloy.command.info;

import alloy.command.util.AbstractCooldownCommand;
import alloy.input.discord.AlloyInputData;
import alloy.main.Alloy;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import net.dv8tion.jda.api.entities.TextChannel;
import utility.time.TimeUtil;

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
        sm.setFrom("UptimeCommand");
        bot.send(sm);
    }

}
