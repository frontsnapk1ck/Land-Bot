package alloy.command.administration;

import alloy.command.util.AbstractModerationCommand;
import alloy.command.util.PunishType;
import alloy.gameobjects.Server;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class MuteCommand extends AbstractModerationCommand {

    @Override
    protected PunishType getPunishType()
    {
        return PunishType.MUTE;
    }

    @Override
    protected DisPerm getRequiredPermission()
    {
        return DisPerm.MANAGE_ROLES;
    }
    
    @Override
    protected boolean punish(Sendable bot, Guild guild, Member member, TextChannel channel) 
    {
        Server s = AlloyUtil.loadServer(guild);
        Role mute = guild.getRoleById(s.getMuteRollID());
        if (mute == null)
            return false;
        
        if (DisPermUtil.checkPermission(member,DisPerm.MOD))
            return false;
        
        try {
            guild.addRoleToMember(member, mute).complete();
        } catch (Exception e)
        {
            return false;
        }

        Template t = Templates.muted(member);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);

        return true;
    }

    
}
