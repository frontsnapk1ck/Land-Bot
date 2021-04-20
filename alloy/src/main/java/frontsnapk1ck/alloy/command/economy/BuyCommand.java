package frontsnapk1ck.alloy.command.economy;

import java.util.List;

import frontsnapk1ck.alloy.command.util.AbstractCooldownCommand;
import frontsnapk1ck.alloy.gameobjects.player.Building;
import frontsnapk1ck.alloy.gameobjects.player.Player;
import frontsnapk1ck.alloy.handler.command.EconHandler;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Queueable;
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
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        List<Building> buildings = AlloyUtil.loadBuildings(g);
        if (!EconHandler.validBuildingName(args[0], buildings)) {
            AlloyTemplate t = Templates.buildingNameNotRecognized(args[0]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Building toBuy = EconHandler.getToBuy(args[0], buildings);
        Player p = AlloyUtil.loadPlayer(g, m);
        if (!EconHandler.canPay(p, toBuy.getCost())) {
            AlloyTemplate t = Templates.bankInsufficientFunds(author, toBuy.getCost());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        p.buyBuilding(toBuy);
        AlloyTemplate t = Templates.buildingBuySuccess(toBuy, author);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);

        addUserCooldown(m, g, handler, getCooldownTime(g), q);
    }
}
