package alloy.command.economy;

import alloy.command.util.AbstractCooldownCommand;
import alloy.gameobjects.player.Player;
import alloy.handler.command.BankHandler;
import alloy.handler.command.PayHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Sendable;
import alloy.main.intefs.handler.CooldownHandler;
import alloy.main.util.SendableMessage;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.StringUtil;
import utility.Util;

public class PayCommand extends AbstractCooldownCommand {

    @Override
    public long getCooldownTime(Guild g) {
        return 10l;
    }

    @Override
    public void execute(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        CooldownHandler handler = data.getCooldownHandler();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);

        if (args.length < 2) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (userOnCooldown(author, g, handler)) {
            Template t = Templates.onCooldown(m);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String numString = args[1];
        int amount = Util.parseInt(numString, -379246534);
        if (amount == -379246534) {
            Template t = Templates.invalidNumberFormat(args);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
        if (amount < BankHandler.MINIUM_BALANCE) {
            Template t = Templates.bankTransferMinimum();
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String message = "";
        if (args.length >= 3)
            message = StringUtil.joinStrings(args, 2);

        Member targetM = AlloyUtil.getMember(g, args[0]);
        Player from = AlloyUtil.loadPlayer(g, m);
        Player to = AlloyUtil.loadPlayer(g, targetM);

        if (PayHandler.canPay(from, amount)) {
            PayHandler.pay(to, from, amount);
            Template t = Templates.bankTransferSuccess(from, to, amount, message);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        } else {
            Template t = Templates.bankInsufficientFunds(author, amount);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

    }

}
