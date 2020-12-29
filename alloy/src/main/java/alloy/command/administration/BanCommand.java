package alloy.command.administration;

import alloy.command.util.AbstractModerationCommand;
import alloy.command.util.PunishType;
import alloy.main.Sendable;
import alloy.utility.discord.perm.DisPerm;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class BanCommand extends AbstractModerationCommand {

    @Override
    public String getDescription() {
        return "bans a member from your guild";
    }

    @Override
    public String getCommand() {
        return "ban";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    protected DisPerm getRequiredPermission() {
        return DisPerm.BAN_MEMBERS;
    }

    @Override
    protected PunishType getPunishType() {
        return PunishType.BAN;
    }

    @Override
    protected boolean punish(Sendable bot, Guild guild, Member member) 
    {
        guild.ban(member, 7).queue();
        return true;
    }
    
}
