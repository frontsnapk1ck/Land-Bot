package alloy.command.economy;

import alloy.command.util.AbstractCommand;
import alloy.gameobjects.player.Player;
import alloy.handler.BankHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.DisUtil;
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
            sm.setFrom("BankCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        MessageEmbed eb = null;
        if (BankHandler.isSend(msg))
            eb = handleSend(p, args, channel, author, msg);

        if (eb != null) {
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("BankCommand");
            sm.setMessage(eb);
            bot.send(sm);
            return;
        }

        Template t = Templates.argumentsNotRecognized(msg);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("BankCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        return;

    }

    private MessageEmbed handleSend(Player p, String[] args, MessageChannel channel, User author, Message msg) {
        String message = "";
        if (BankHandler.hasMessage(args))
            message = BankHandler.getMessage(args);
        User targetUser = BankHandler.getTargetUser(args);
        Member targetMember = msg.getGuild().getMember(targetUser);
        int amount = BankHandler.getAmount(args);

        if (amount == BankHandler.INVALID_FORMAT) {
            Template t = Templates.invalidNumberFormat(args);
            return t.getEmbed();
        }

        if (amount < 0) {
            Template t = Templates.onlyPositiveNumbers(amount);
            return t.getEmbed();
        }

        if (amount < BankHandler.MINIUM_BALANCE) {
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
