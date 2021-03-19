package frontsnapk1ck.alloy.command.administration;

import frontsnapk1ck.alloy.command.util.AbstractModerationCommand;
import frontsnapk1ck.alloy.command.util.PunishType;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.disterface.util.template.Template;
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
        return DisPerm.MOD;
    }
    
    @Override
    protected String punish(Sendable bot, Guild guild, Member member, TextChannel channel) 
    {
        Server s = AlloyUtil.loadServer(guild);
        Role mute = guild.getRoleById(s.getMuteRollID());
        if (mute == null)
            return "No Mute Role set in this server";
                
        try 
        {
            guild.addRoleToMember(member, mute).complete();

            Template t = Templates.muted(member);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);

            return SUCCESS;
        } catch (Exception e)
        {
            return e.getMessage();
        }
    }

    
}
