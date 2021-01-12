package alloy.command.configuration;

import alloy.command.util.AbstractCommand;
import alloy.gameobjects.Server;
import alloy.handler.RankHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.DisUtil;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.Util;

public class SetCommand extends AbstractCommand {

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
            sm.setFrom("SetCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args.length < 1) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("SetCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args[0].equalsIgnoreCase("spam"))
            setSpam(data);
        else if (args[0].equalsIgnoreCase("xp"))
            setXp(data);
    }

    private void setXp(AlloyInputData data) {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        User tarU = DisUtil.parseUser(args[1]);

        if (tarU == null) {
            Template t = Templates.userNotFound(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("SetCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Member target = g.getMember(tarU);

        if (target == null) {
            Template t = Templates.userNotFound(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("SetCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!Util.validInt(args[2])) {
            Template t = Templates.invalidNumberFormat(args[2]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RankupCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        int xp = Integer.parseInt(args[2]);

        RankHandler.setXP(target, xp);
        Template t = Templates.xpSetSuccess(target, xp);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("RankupCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        return;

    }

    private void setSpam(AlloyInputData data) {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (args.length < 2) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("SetCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        TextChannel target = DisUtil.findChannel(g, DisUtil.mentionToId(args[1]));
        if (target == null) {
            Template t = Templates.invalidChannel(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("SetCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Server s = AlloyUtil.loadServer(g);
        s.changeSpamChannel(target.getIdLong());
        Template t = Templates.spamChannelChanged(target);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("SetCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);

    }

}
