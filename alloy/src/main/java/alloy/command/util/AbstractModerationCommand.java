package alloy.command.util;

import alloy.gameobjects.Case;
import alloy.gameobjects.Server;
import alloy.handler.CaseHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Moderator;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.DisUtil;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.StringUtil;

public abstract class AbstractModerationCommand extends AbstractCommand {

    protected abstract DisPerm getRequiredPermission();

    protected abstract PunishType getPunishType();

    protected abstract boolean punish(Sendable bot, Guild guild, Member member);

    @Override
    public String[] getUsage() {
        return new String[] {
                String.format("%s <user>     //%s user from guild", getCommand(), getPunishType().getDescription()), };
    }

    @Override
    public void execute(AlloyInputData e) {
        TextChannel chan = e.getChannel();
        Guild g = chan.getGuild();
        User author = e.getUser();
        String[] args = AlloyInputUtil.getArgs(e);
        Sendable bot = e.getSendable();
        Moderator mod = e.getModerator();
        Message msg = e.getMessageActual();

        MessageEmbed fail = checkPermissions(g, author, chan);
        if (fail != null) {
            SendableMessage sm = new SendableMessage();
            sm.setChannel(chan);
            sm.setFrom("AbstractModerationCommand");
            sm.setMessage(fail);
            bot.send(sm);
            return;
        }

        if (args.length == 0) {
            Template t = Templates.moderationActionEmpty(chan, getPunishType());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(chan);
            sm.setFrom("AbstractModerationCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Member targetUser = DisUtil.findMember(g, args);
        if (targetUser == null) {
            Template t = Templates.userNotFound(args[0]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(chan);
            sm.setFrom("AbstractModerationCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (author.getId().equals(g.getSelfMember().getId())) {
            Template t = Templates.cannotModerateSelf();
            SendableMessage sm = new SendableMessage();
            sm.setChannel(chan);
            sm.setFrom("AbstractModerationCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (DisPermUtil.isMod(targetUser.getPermissions())) {
            Template t = Templates.cannotModerateModerators();
            SendableMessage sm = new SendableMessage();
            sm.setChannel(chan);
            sm.setFrom("AbstractModerationCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        boolean success = punish(bot, g, targetUser);
        if (!success) {
            Template t = Templates.moderationActionFailed(getPunishType());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(chan);
            sm.setFrom("AbstractModerationCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        int caseID = CaseHandler.nextID(g);
        TextChannel modLog = mod.getModLogChannel(g.getIdLong());
        if (modLog != null) {
            String message = StringUtil.joinStrings(args, 1);
            Case c = CaseHandler.buildCase(caseID, author, getPunishType(), message, targetUser, msg);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(modLog);
            sm.setFrom("AbstractModerationCommand");
            sm.setMessage(CaseHandler.toEmbed(c));
            bot.send(sm);
        }

        if (modLog == null) {
            Template t = Templates.modlogNotFound();
            SendableMessage sm = new SendableMessage();
            sm.setChannel(chan);
            sm.setFrom("AbstractModerationCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Template log = Templates.moderationLog(chan, chan.getGuild(), author, this.getPunishType(),
                fixModLogArgs(args, g));
        Server s = AlloyUtil.loadServer(chan.getGuild());
        TextChannel tc = DisUtil.findChannel(chan.getGuild(), s.getModLogChannel());
        SendableMessage sm = new SendableMessage();
        sm.setChannel(tc);
        sm.setFrom("AbstractModerationCommand");
        sm.setMessage(log.getEmbed());
        bot.send(sm);

        Template t = Templates.moderationActionSuccess(chan, targetUser, getPunishType().getVerb());
        SendableMessage sm2 = new SendableMessage();
        sm.setChannel(chan);
        sm.setFrom("AbstractModerationCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm2);
    }

    private String[] fixModLogArgs(String[] args, Guild g) {
        String[] newArr = new String[args.length];

        int i = 0;
        for (String s : args) {
            if (DisUtil.isRole(s, g))
                s = DisUtil.parseRole(s, g).getName();
            else if (DisUtil.isUserMention(s))
                s = DisUtil.findMember(g, s).getUser().getAsTag();
            else if (DisUtil.isValidChannel(g, s))
                s = DisUtil.findChannel(g, s).getName();
            newArr[i] = s;
            i++;
        }

        return newArr;
    }

    private MessageEmbed checkPermissions(Guild g, User author, TextChannel chan) {
        if (getRequiredPermission() != null) {
            if (!DisPermUtil.checkPermission(g.getMember(author), getRequiredPermission())) {
                Template t = Templates.noPermission(getRequiredPermission(), author);
                return t.getEmbed();
            }

            if (!DisPermUtil.checkPermission(g.getSelfMember(), getRequiredPermission())) {
                Template t = Templates.noPermission(getRequiredPermission(), g.getSelfMember().getUser());
                return t.getEmbed();
            }
        }
        return null;
    }

}
