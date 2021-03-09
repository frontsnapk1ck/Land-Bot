package alloy.command.configuration;

import alloy.command.util.AbstractCommand;
import alloy.handler.command.CooldownHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.templates.Templates;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.Util;

public class CooldownCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() {
        return DisPerm.ADMINISTRATOR;
    }

    @Override
    public void execute(AlloyInputData data) {
        String[] args = AlloyInputUtil.getArgs(data);

        if (args.length > 0 && args[0].equalsIgnoreCase("xp"))
            xpCooldown(data);
        else
            cooldown(data);
    }

    private void xpCooldown(AlloyInputData data) {
        String[] args = AlloyInputUtil.getArgs(data);

        if (args.length == 1)
            CooldownHandler.showXPCooldown(data);
        else if (args.length == 2)
            changeXPCooldown(data);
    }

    private void changeXPCooldown(AlloyInputData data) {
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

        try {
            int newTime = Util.parseInt(args[1]);
            CooldownHandler.setXpCooldown(g, newTime);
            CooldownHandler.showXPCooldown(data);
        } catch (NumberFormatException e) {
            Template t = Templates.invalidNumberFormat(args);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

    }

    private void cooldown(AlloyInputData data) {
        String[] args = AlloyInputUtil.getArgs(data);

        if (args.length == 0)
            CooldownHandler.showCooldown(data);
        else if (args.length == 1)
            changeCooldown(data);
    }

    private void changeCooldown(AlloyInputData data) {
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

        try {
            int newTime = Util.parseInt(args[0]);
            CooldownHandler.setCooldown(g, newTime);
            CooldownHandler.showCooldown(data);
        } catch (NumberFormatException e) {
            Template t = Templates.invalidNumberFormat(args);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
    }

}
