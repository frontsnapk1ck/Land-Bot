package frontsnapk1ck.alloy.command.util;

import frontsnapk1ck.alloy.handler.command.AdminHandler;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Moderator;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.DisUtil;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import frontsnapk1ck.disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public abstract class AbstractModerationCommand extends AbstractCommand {

    /**
     * the response of a moderation action if there were no 
     *  errors in the operation of the action 
     *
     */
    public static final String SUCCESS = "SUCCESS";

    /**
     * to be implemented by the commands themselves with their different moderation actions
     * @return the permission that is required to use this command
     */
    protected abstract DisPerm getRequiredPermission();

    /**
     * to be implemented by the commands themselves with their different moderation actions
     * @return the type of punishment that this command uses
     */
    protected abstract PunishType getPunishType();

    /**
     * 
     * punishes a member in a guild. <p></p>
     * to be implemented by the commands themselves with their different moderation actions
     * 
     * @param bot the sendable that messages will be sent back through
     * @param guild the server in which the action is taking place
     * @param member the member that is being punished
     * @param chan the channel in which the punishment is taking place
     * 
     * @return a message regarding the success of the punishment
     *          if there was a failure it will return a fail message, 
     *          otherwise it will return {@code "SUCCESS"}
     */
    protected abstract String punish(Sendable bot, Guild guild, Member member, TextChannel chan);

    @Override
    public void execute(AlloyInputData e) 
    {
        TextChannel chan = e.getChannel();
        Guild g = chan.getGuild();
        User author = e.getUser();
        String[] args = AlloyInputUtil.getArgs(e);
        Sendable bot = e.getSendable();
        Moderator mod = e.getModerator();
        Message msg = e.getMessageActual();

        MessageEmbed fail = checkPermissions(g, author, chan);
        
        if (fail != null) 
        {
            SendableMessage sm = new SendableMessage();
            sm.setChannel(chan);
            sm.setFrom(getClass());
            sm.setMessage(fail);
            bot.send(sm);
            return;
        }

        if (args.length == 0) 
        {
            Template t = Templates.moderationActionEmpty(chan, getPunishType());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(chan);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Member targetUser = DisUtil.findMember(g, args);
        if (targetUser == null) 
        {
            Template t = Templates.userNotFound(args[0]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(chan);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (author.getId().equals(g.getSelfMember().getId())) 
        {
            Template t = Templates.cannotModerateSelf();
            SendableMessage sm = new SendableMessage();
            sm.setChannel(chan);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (DisPermUtil.isMod(targetUser.getPermissions())) 
        {
            Template t = Templates.cannotModerateModerators();
            SendableMessage sm = new SendableMessage();
            sm.setChannel(chan);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String successS = punish(bot, g, targetUser, chan);
        boolean success = successS.equalsIgnoreCase(SUCCESS);
        if (!success) 
        {
            Template t = Templates.moderationActionFailed(getPunishType() , successS);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(chan);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        AdminHandler.makeCase(bot, mod, getPunishType(), chan, msg, targetUser);

        Template t = Templates.moderationActionSuccess(chan, targetUser, getPunishType().getVerb());
        SendableMessage sm = new SendableMessage();
        sm.setChannel(chan);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

    private MessageEmbed checkPermissions(Guild g, User author, TextChannel chan) 
    {
        if (getRequiredPermission() != null) 
        {
            if (!DisPermUtil.checkPermission(g.getMember(author), getRequiredPermission())) 
            {
                Template t = Templates.noPermission(getRequiredPermission(), author);
                return t.getEmbed();
            }

            if (!DisPermUtil.checkPermission(g.getSelfMember(), getRequiredPermission())) 
            {
                Template t = Templates.noPermission(getRequiredPermission(), g.getSelfMember().getUser());
                return t.getEmbed();
            }
        }
        return null;
    }

}
