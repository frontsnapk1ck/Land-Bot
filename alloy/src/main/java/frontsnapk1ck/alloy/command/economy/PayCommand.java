package frontsnapk1ck.alloy.command.economy;

import frontsnapk1ck.alloy.command.util.AbstractCooldownCommand;
import frontsnapk1ck.alloy.gameobjects.player.Player;
import frontsnapk1ck.alloy.handler.command.EconHandler;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.intefs.handler.CooldownHandler;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import frontsnapk1ck.utility.StringUtil;
import frontsnapk1ck.utility.Util;

public class PayCommand extends AbstractCooldownCommand {

    @Override
    public long getCooldownTime(Guild g) {
        return 10l;
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        CooldownHandler handler = data.getCooldownHandler();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);

        if (args.length < 2) {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (userOnCooldown(author, g, handler)) {
            AlloyTemplate t = Templates.onCooldown(m);
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
            AlloyTemplate t = Templates.invalidNumberFormat(args);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
        if (amount < EconHandler.MINIUM_BALANCE) {
            AlloyTemplate t = Templates.bankTransferMinimum();
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

        if (EconHandler.canPay(from, amount)) {
            EconHandler.pay(to, from, amount);
            AlloyTemplate t = Templates.bankTransferSuccess(from, to, amount, message);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        } else {
            AlloyTemplate t = Templates.bankInsufficientFunds(author, amount);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

    }

}
