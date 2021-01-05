package alloy.command.administration;

import alloy.command.util.AbstractModerationCommand;
import alloy.command.util.PunishType;
import alloy.main.Sendable;
import alloy.utility.discord.perm.DisPerm;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class KickCommand extends AbstractModerationCommand {

    @Override
    public String getDescription()
    {
        return "Kicks a member from your guild";
    }

    @Override
    public String getCommand()
    {
        return "kick";
    }

    @Override
    public String[] getAliases()
    {
        return new String[0];
    }

    @Override
    protected PunishType getPunishType()
    {
        return PunishType.KICK;
    }

    @Override
    protected DisPerm getRequiredPermission()
    {
        return DisPerm.KICK_MEMBERS;
    }

    @Override
    protected boolean punish(Sendable bot, Guild guild, Member member) 
    {
        guild.kick(member).queue();
        return true;
    }

}
