package alloy.command.economy;

import java.util.List;

import alloy.command.util.AbstractCooldownCommand;
import alloy.gameobjects.player.Building;
import alloy.gameobjects.player.Player;
import alloy.handler.command.BuyHandler;
import alloy.handler.command.PayHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Queueable;
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

public class BuyCommand extends AbstractCooldownCommand {

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
        Queueable q = data.getQueue();

        if (args.length == 0) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        List<Building> buildings = AlloyUtil.loadBuildings(g);
        if (!BuyHandler.validBuildingName(args[0], buildings)) {
            Template t = Templates.buildingNameNotRecognized(args[0]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Building toBuy = BuyHandler.getToBuy(args[0], buildings);
        Player p = AlloyUtil.loadPlayer(g, m);
        if (!PayHandler.canPay(p, toBuy.getCost())) {
            Template t = Templates.bankInsufficientFunds(author, toBuy.getCost());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        p.buyBuilding(toBuy);
        Template t = Templates.buildingBuySuccess(toBuy, author);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);

        addUserCooldown(m, g, handler, getCooldownTime(g), q);
    }
}
