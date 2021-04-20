package frontsnapk1ck.alloy.command.administration;

import java.util.List;

import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.gameobjects.Case;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.handler.command.AdminHandler;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.discord.DisUtil;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import frontsnapk1ck.utility.StringUtil;
import frontsnapk1ck.utility.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class CaseCommand extends AbstractCommand {


    @Override
    public DisPerm getPermission() 
    {
        return DisPerm.MANAGER;
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild guild = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (!DisPermUtil.checkPermission(guild.getMember(author), getPermission())) 
        {
            AlloyTemplate t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args.length < 1) 
        {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args[0].equalsIgnoreCase("show"))
            show(data);
        if (args[0].equalsIgnoreCase("reason"))
            reason(data);
        if (args[0].equalsIgnoreCase("user"))
            user(data);
    }

    private void show(AlloyInputData data) 
    {
        Guild guild = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (args.length < 2) 
        {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!Util.validInt(args[1])) 
        {
            AlloyTemplate t = Templates.invalidNumberFormat(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Case c = AdminHandler.getCase(guild.getIdLong(), args[1]);
        MessageEmbed e;
        if (c == null)
            e = Templates.caseNotFound(args[1]).getEmbed();
        else
            e = AdminHandler.toEmbed(c);

        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(e);
        bot.send(sm);
    }

    private void reason(AlloyInputData data) 
    {
        Guild guild = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        MessageEmbed embed = editReason(bot, guild, guild.getMember(author), channel, args[1],
        StringUtil.joinStrings(args, 2));
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(embed);
        bot.send(sm);
        return;
    }

    private void user(AlloyInputData data) 
    {
        Guild guild = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        Member target = DisUtil.findMember(guild, args[1]);
        if (target == null)
        {
            AlloyTemplate t = Templates.userNotFound(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
        List<Case> cases = AdminHandler.allCases(target);

        AlloyTemplate t = Templates.caseList(cases);
        SendableMessage sm = new SendableMessage();
        sm.setFrom(getClass());
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        return;
    }

    private MessageEmbed editReason(Sendable bot, Guild guild, Member moderator, MessageChannel channel, String caseId, String reason) 
    {
        Case c = AdminHandler.getCase(guild.getIdLong(), caseId);
        if (c == null) {
            AlloyTemplate t = Templates.caseNotFound(caseId);
            return t.getEmbed();
        }

        c.setReason(reason);

        Server s = AlloyUtil.loadServer(guild);
        TextChannel tc = guild.getTextChannelById(s.getModLogChannel());
        if (tc == null) {
            AlloyTemplate t = Templates.modlogNotFound();
            return t.getEmbed();
        }

        Message m = tc.getHistory().getMessageById(c.getMessageId());
        try {
            m.editMessage(AdminHandler.toEmbed(c)).complete();
        } catch (Exception e) 
        {
            AlloyTemplate t = Templates.caseEditFailed();
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
    
        }

        AlloyTemplate t = Templates.caseReasonModified(reason);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);

        return null;
    }

}
