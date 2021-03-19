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

/**
 * Bans a given user from your guild
 */
public class BanCommand extends AbstractModerationCommand {

    /**
     * this command needs the {@code BAN_MEMBERS} permission
     */
    @Override
    protected DisPerm getRequiredPermission()
    {
        return DisPerm.BAN_MEMBERS;
    }

    /**
     * this command's {@link PunishType} is {@code BAN}
     */
    @Override
    protected PunishType getPunishType()
    {
        return PunishType.BAN;
    }

   /**
     * 
     * bans a member from this guild
     * 
     * @param bot the sendable that messages will be sent back through
     * @param guild the server in which the action is taking place
     * @param member the member that is being punished
     * @param chan the channel in which the punishment is taking place
     */
    @Override
    protected String punish(Sendable bot, Guild guild, Member member, TextChannel channel) 
    {
        User u = member.getUser();

        try 
        {
            guild.ban(member, 7).complete();

            Template t = Templates.banned(u);
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
