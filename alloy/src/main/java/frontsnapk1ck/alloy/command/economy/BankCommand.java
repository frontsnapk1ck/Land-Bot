package frontsnapk1ck.alloy.command.economy;

import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.gameobjects.player.Player;
import frontsnapk1ck.alloy.handler.command.EconHandler;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.discord.DisUtil;
import frontsnapk1ck.disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BankCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) {
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Message msg = data.getMessageActual();
        Guild g = data.getGuild();
        Member m = g.getMember(author);

        Player p = null;

        if (args.length == 0) 
            p = AlloyUtil.loadPlayer(m);
        else if (DisUtil.findMember(g, args[0]) != null) 
            p = AlloyUtil.loadPlayer(DisUtil.findMember(g, args[0]));

        if (p != null) {
            Template t = Templates.bankCurrentBalance(p);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        MessageEmbed eb = null;
        if (EconHandler.isSend(msg))
            eb = handleSend(p, args, channel, author, msg);

        if (eb != null) {
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(eb);
            bot.send(sm);
            return;
        }

        Template t = Templates.argumentsNotRecognized(msg);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        return;

    }

    private MessageEmbed handleSend(Player p, String[] args, MessageChannel channel, User author, Message msg) {
        String message = "";
        if (EconHandler.hasMessage(args))
            message = EconHandler.getMessage(args);
        User targetUser = EconHandler.getTargetUser(args);
        Member targetMember = msg.getGuild().getMember(targetUser);
        int amount = EconHandler.getAmount(args);

        if (amount == EconHandler.INVALID_FORMAT) {
            Template t = Templates.invalidNumberFormat(args);
            return t.getEmbed();
        }

        if (amount < 0) {
            Template t = Templates.onlyPositiveNumbers(amount);
            return t.getEmbed();
        }

        if (amount < EconHandler.MINIUM_BALANCE) {
            Template t = Templates.bankTransferMinimum();
            return t.getEmbed();
        }

        if (!p.canSpend(amount)) {
            Template t = Templates.bankInsufficientFunds(author, amount);
            return t.getEmbed();
        }

        if (targetUser == null) {
            Template t = Templates.userNotFound(args[0]);
            return t.getEmbed();
        }

        Player targetP = AlloyUtil.loadPlayer(targetMember);

        targetP.addBal(amount);
        p.spend(amount);

        Template t = Templates.bankTransferSuccess(p, targetP, amount, message);
        return t.getEmbed();
    }

}
