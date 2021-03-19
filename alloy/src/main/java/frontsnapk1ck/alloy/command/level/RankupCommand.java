package frontsnapk1ck.alloy.command.level;

import java.util.List;

import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.gameobjects.RankUp;
import frontsnapk1ck.alloy.gameobjects.player.Rank;
import frontsnapk1ck.alloy.handler.command.ConfigHandler;
import frontsnapk1ck.alloy.handler.command.FunHandler;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import frontsnapk1ck.disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import frontsnapk1ck.utility.Util;

public class RankupCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() {
        return DisPerm.ADMINISTRATOR;
    }

    @Override
    public void execute(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);

        if (!DisPermUtil.checkPermission(m, getPermission())) {
            Template t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args.length < 1) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args[0].equalsIgnoreCase("add"))
            add(args, bot, channel, g);
        else if (args[0].equalsIgnoreCase("remove"))
            remove(args, bot, channel, g);
        else if (args[0].equalsIgnoreCase("view"))
            view(args, bot, channel, g);
        else if (args[0].equalsIgnoreCase("test"))
            test(args, bot, channel, m);
    }

    private void add(String[] args, Sendable bot, TextChannel channel, Guild g) {
        if (args.length < 3) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (ConfigHandler.hasRole(args, g))
            ConfigHandler.addRankupRole(args, bot, channel, g);
        else
            ConfigHandler.addRankup(args, bot, channel, g);

        view(args, bot, channel, g);
    }

    private void remove(String[] args, Sendable bot, TextChannel channel, Guild g) {
        if (args.length < 2) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!Util.validInt(args[1])) {
            Template t = Templates.invalidNumberFormat(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        int level = Integer.parseInt(args[1]);

        if (!ConfigHandler.containsLevel(g, level)) {
            Template t = Templates.levelNotFound(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        ConfigHandler.removeRankup(level, g, channel, bot);
        view(args, bot, channel, g);
    }

    private void view(String[] args, Sendable bot, TextChannel channel, Guild g) {
        List<RankUp> rus = ConfigHandler.loadRankups(g);
        Template t = Templates.viewRankUps(rus);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        return;
    }

    private void test(String[] args, Sendable bot, TextChannel channel, Member m) {
        if (args.length < 2) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!Util.validInt(args[1])) {
            Template t = Templates.invalidNumberFormat(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        int level = Integer.parseInt(args[1]);
        Rank r = ConfigHandler.getRank(level);

        FunHandler.announceRankUp(r, m, false, bot, channel);

    }

}
