package alloy.command.info;

import alloy.command.util.AbstractCooldownCommand;
import alloy.handler.PrefixHandeler;
import alloy.input.discord.AlloyInputData;
import alloy.main.Alloy;
import alloy.main.Sendable;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class PingMeCommand extends AbstractCooldownCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        PrefixHandeler.viewPrefix(channel, bot, g);
        Alloy.LOGGER.debug("PingMeCommand", "pinged in guild " + g.getName() + "\tid: " + g.getId());
    }
    
}
