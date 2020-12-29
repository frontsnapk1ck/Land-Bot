package alloy.command.configuration;

import alloy.command.util.AbstractCommand;
import alloy.handler.PrefixHandeler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Sendable;
import alloy.utility.discord.perm.DisPerm;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class PrefixCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission()
	{
		return DisPerm.ADMINISTRATOR;
	}

	@Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (args.length > 0 )
            PrefixHandeler.changePrefix( g , args[0] );

        PrefixHandeler.viewPrefix( channel, bot, g);
    }
    
}
