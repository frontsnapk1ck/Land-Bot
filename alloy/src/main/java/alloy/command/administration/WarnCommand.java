package alloy.command.administration;

import alloy.command.util.AbstractCommand;
import alloy.gameobjects.Warning;
import alloy.gameobjects.player.Player;
import alloy.handler.command.FunChatHandler;
import alloy.handler.command.WarningHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.DisUtil;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class WarnCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() 
    {
        return DisPerm.MOD;    
    }

    @Override
    public void execute(AlloyInputData data) {

        Guild g = data.getGuild();
        TextChannel chan = data.getChannel();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);

        if (!DisPermUtil.checkPermission(m, getPermission())) 
        {
            warnGif(data);
            return;
        }

        if (args.length < 2) 
        {
            warnGif(data);
            return;
        }

        if (args[0].equalsIgnoreCase("del")) {
            delwarn(data);
            return;
        }

        Member target = DisUtil.findMember(g, args[0]);

        if (target == null) {
            Template t = Templates.userNotFound(args[0]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WarnCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Member warner = g.getMember(author);
        if (!DisPermUtil.checkPermission(warner, getPermission())) {
            Template t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WarnCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Warning w = WarningHandler.newWarning(args, (TextChannel) channel, target, author, g);

        Player p = AlloyUtil.loadPlayer(g, m);

        p.addWarning(w);
        PrivateChannel pc = target.getUser().openPrivateChannel().complete();
        if (pc == null) {
            Template t = Templates.privateMessageFailed(m);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WarnCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Template warn = Templates.getWarn(w);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(pc);
        sm.setFrom("WarnCommand");
        sm.setMessage(warn.getEmbed());
        bot.send(sm);

        Template t = Templates.warnSuccess(target, w, author);
        SendableMessage sm2 = new SendableMessage();
        sm2.setChannel(chan);
        sm2.setFrom("WarnCommand");
        sm2.setMessage(t.getEmbed());
        bot.send(sm2);
    }

    private void warnGif(AlloyInputData data) 
    {
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        SendableMessage sm = new SendableMessage();

        String[] chats = FunChatHandler.getWarnChats();
        int i = (int) (Math.random() * chats.length);
        String message = chats[i];

        sm.setChannel(channel);
        sm.setFrom("CooldownCommand");
        sm.setMessage(message);
        bot.send(sm);
    }

    private void delwarn(AlloyInputData data) {
        Guild guild = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = guild.getMember(author);

        if (!DisPermUtil.checkPermission(m, getPermission())) {
            Template t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RemoveWarnings");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!WarningHandler.warningExists(guild, args[1])) {
            Template t = Templates.warningNotFound(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RemoveWarnings");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Member warned = WarningHandler.removeWarnings(guild, args[1]);

        Template t = Templates.warningsRemovedSuccess(args[0], warned);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("RemoveWarnings");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

}
