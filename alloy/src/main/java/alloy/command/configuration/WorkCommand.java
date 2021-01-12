package alloy.command.configuration;

import alloy.command.util.AbstractCommand;
import alloy.handler.WorkHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.perm.DisPermUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class WorkCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);
        Message msg = data.getMessageActual();

        if (!DisPermUtil.checkPermission(m, getPermission())) {
            Template t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WorkCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args.length == 0) {
            Template t = Templates.argumentsNotRecognized(msg);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WorkCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args[0].equalsIgnoreCase("add"))
            addWork(data);
        else if (args[0].equalsIgnoreCase("rm") || args[0].equalsIgnoreCase("remove"))
            removeWork(data);
        else if (args[0].equalsIgnoreCase("reset"))
            resetWork(data);

        Template t = Templates.workOptions(g);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("WorkCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

    private void addWork(AlloyInputData data) {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Message msg = data.getMessageActual();

        if (args.length == 1) {
            Template t = Templates.argumentsNotRecognized(msg);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WorkCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        WorkHandler.addWorkOption(g, args);

        Template t = Templates.workOptionAddSuccess(args);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("WorkCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);

    }

    private void removeWork(AlloyInputData data) {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (args.length < 1) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WorkCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        try {
            int i = Integer.parseInt(args[0]);
            String s = WorkHandler.removeWork(i - 1, g);
            Template t = Templates.workRemoveSuccess(s);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WorkCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        } catch (NumberFormatException e) {
            Template t = Templates.invalidNumberFormat(args);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WorkCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        } catch (IndexOutOfBoundsException e) {
            Template t = Templates.numberOutOfBounds(e);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WorkCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

    }

    private void resetWork(AlloyInputData data) {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        WorkHandler.resetWork(g);
        Template t = Templates.workOptions(g);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("WorkCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

}
