package alloy.command.administration;

import alloy.command.util.AbstractModerationCommand;
import alloy.command.util.PunishType;
import alloy.gameobjects.Server;
import alloy.main.Sendable;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.perm.DisPerm;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class UnmuteCommand extends AbstractModerationCommand {

    @Override
    protected DisPerm getRequiredPermission() 
    {
        return DisPerm.MANAGE_ROLES;
    }

    @Override
    protected PunishType getPunishType() 
    {
        return PunishType.UNMUTE;
    }

    @Override
    protected boolean punish(Sendable bot, Guild guild, Member member) 
    {
        Server s = AlloyUtil.loadServer(guild);
        Role mute = guild.getRoleById(s.getMuteRollID());
        if (mute == null)
            return false;
        
        guild.removeRoleFromMember(member, mute).queue();
        return true;
    }

}
