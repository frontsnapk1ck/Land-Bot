package frontsnapk1ck.alloy.command.administration;

import frontsnapk1ck.alloy.command.util.AbstractModerationCommand;
import frontsnapk1ck.alloy.command.util.PunishType;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class KickCommand extends AbstractModerationCommand {

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
    protected String punish(Sendable bot, Guild guild, Member member, TextChannel channel) 
    {
        User u = member.getUser();

        try {
            guild.kick(member).complete();

            Template t = Templates.kicked(u);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);            

            return SUCCESS;
        }
        catch (Exception e) 
        {
            return e.getMessage();
        }
        
    }

}
