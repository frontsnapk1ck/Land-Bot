package frontsnapk1ck.alloy.command.configuration;

import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.handler.command.ConfigHandler;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.DisUtil;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class XPBlacklistCommand extends AbstractCommand {

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
            AlloyTemplate t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        boolean view = args.length == 0 || args[0].equalsIgnoreCase("view");
        if (view)
            ConfigHandler.view(g, channel, bot);

        if (args.length != 2) {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args[0].equalsIgnoreCase("add"))
            add(args[1], bot, channel, g);
        else if (args[0].equalsIgnoreCase("remove"))
            remove(args[1], bot, channel, g);

    }

    private void add(String c, Sendable bot, TextChannel channel, Guild g) {
        if (!DisUtil.isValidChannel(g, c)) {
            AlloyTemplate t = Templates.invalidChannel(c);
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (ConfigHandler.isBlacklisted(g, channel.getAsMention())) {
            AlloyTemplate t = Templates.channelIsAlreadyBlacklisted(channel);
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        ConfigHandler.add(g, c);

        AlloyTemplate t = Templates.blackListAddSuccess(c);
        SendableMessage sm = new SendableMessage();
        sm.setFrom(getClass());
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        bot.send(sm);

        ConfigHandler.view(g, channel, bot);
    }

    private void remove(String c, Sendable bot, TextChannel channel, Guild g) {
        if (!DisUtil.isValidChannel(g, c)) {
            AlloyTemplate t = Templates.invalidChannel(c);
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!ConfigHandler.isBlacklisted(g, c)) {
            AlloyTemplate t = Templates.channelIsNotBlacklisted(c);
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        ConfigHandler.remove(g, c);
        AlloyTemplate t = Templates.blackListRemoveSuccess(c);
        SendableMessage sm = new SendableMessage();
        sm.setFrom(getClass());
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        bot.send(sm);

        ConfigHandler.view(g, channel, bot);
    }
}
