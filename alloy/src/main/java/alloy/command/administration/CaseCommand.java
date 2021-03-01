package alloy.command.administration;

import java.util.function.Consumer;

import alloy.command.util.AbstractCommand;
import alloy.gameobjects.Case;
import alloy.gameobjects.Server;
import alloy.handler.CaseHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Alloy;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;
import utility.StringUtil;
import utility.Util;

public class CaseCommand extends AbstractCommand {


    @Override
    public DisPerm getPermission() {
        return DisPerm.ADMINISTRATOR;
    }

    @Override
    public void execute(AlloyInputData data) {
        Guild guild = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (!DisPermUtil.checkPermission(guild.getMember(author), getPermission())) {
            Template t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("AbstractModerationCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        boolean show = args.length > 0 && args[0].equalsIgnoreCase("show") && args.length < 3;
        boolean reason = args.length > 0 && args[0].equalsIgnoreCase("reason");
        boolean validInt = Util.validInt(args[1]);
        if (reason) {
            MessageEmbed embed = editReason(bot, guild, guild.getMember(author), channel, args[1],
                    StringUtil.joinStrings(args, 2));

            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("AbstractModerationCommand");
            sm.setMessage(embed);
            bot.send(sm);
            return;
        }

        if (show && validInt) {
            Case c = CaseHandler.getCase(guild.getIdLong(), args[1]);
            MessageEmbed e;
            if (c == null)
                e = Templates.caseNotFound(args[1]).getEmbed();
            else
                e = CaseHandler.toEmbed(c);

            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("AbstractModerationCommand");
            sm.setMessage(e);
            bot.send(sm);
            return;
        }

        Template t = Templates.invalidUse(channel);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("AbstractModerationCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        return;
    }

    private MessageEmbed editReason(Sendable bot, Guild guild, Member moderator, MessageChannel channel, String caseId, String reason) 
    {
        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>() 
        {
            @Override
            public void accept(ErrorResponseException t) 
            {
                Alloy.LOGGER.warn("DeleteMessageJob", t.getMessage());
            }

            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE, consumer);

        Case c = CaseHandler.getCase(guild.getIdLong(), caseId);
        if (c == null) {
            Template t = Templates.caseNotFound(caseId);
            return t.getEmbed();
        }

        c.setReason(reason);

        Server s = AlloyUtil.loadServer(guild);
        TextChannel tc = guild.getTextChannelById(s.getModLogChannel());
        if (tc == null) {
            Template t = Templates.modlogNotFound();
            return t.getEmbed();
        }

        Message m = tc.getHistory().getMessageById(c.getMessageId());
        m.editMessage(CaseHandler.toEmbed(c)).queue(null, handler);

        Template t = Templates.caseReasonModified(reason);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("AbstractModerationCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);

        return null;
    }

}
