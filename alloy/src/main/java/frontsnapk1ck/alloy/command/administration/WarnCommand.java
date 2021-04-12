package frontsnapk1ck.alloy.command.administration;

import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.command.util.PunishType;
import frontsnapk1ck.alloy.gameobjects.Warning;
import frontsnapk1ck.alloy.gameobjects.player.Player;
import frontsnapk1ck.alloy.handler.command.AdminHandler;
import frontsnapk1ck.alloy.handler.command.FunHandler;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Moderator;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.discord.DisUtil;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import frontsnapk1ck.disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
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
        Moderator mod = data.getModerator();
        PunishType punishType = PunishType.WARN;
        Message msg = data.getMessageActual();

        if (!DisPermUtil.checkPermission(m, getPermission())) 
        {
            warnGif(data);
            return;
        }

        else if (args.length < 2) 
        {
            warnGif(data);
            return;
        }

        if (args[0].equalsIgnoreCase("del")) 
        {
            delwarn(data);
            return;
        }

        Member target = DisUtil.findMember(g, args[0]);

        if (target == null) 
        {
            Template t = Templates.userNotFound(args[0]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Member warner = g.getMember(author);
        if (!DisPermUtil.checkPermission(warner, getPermission())) 
        {
            Template t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Warning w = AdminHandler.newWarning(args, (TextChannel) channel, target, author, g);

        Player p = AlloyUtil.loadPlayer(g, m);

        p.addWarning(w);
        try {
            PrivateChannel pc = target.getUser().openPrivateChannel().complete();
                
            Template warn = Templates.getWarn(w);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(pc);
            sm.setFrom(getClass());
            sm.setMessage(warn.getEmbed());
            bot.send(sm);   
            
        }
        catch (Exception e) 
        {
            Template t = Templates.privateMessageFailed(m);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }

        try 
        {
            AdminHandler.makeCase(bot, mod, punishType, chan, msg, target);    
        }
        catch (NullPointerException e) 
        {
            
        }
    
        Template t = Templates.warnSuccess(target, w, author);
        SendableMessage sm2 = new SendableMessage();
        sm2.setChannel(chan);
        sm2.setFrom(getClass());
        sm2.setMessage(t.getEmbed());
        bot.send(sm2);
    }

    private void warnGif(AlloyInputData data) 
    {
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        SendableMessage sm = new SendableMessage();

        String[] chats = FunHandler.getWarnChats();
        int i = (int) (Math.random() * chats.length);
        String message = chats[i];

        sm.setChannel(channel);
        sm.setFrom(getClass());
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
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!AdminHandler.warningExists(guild, args[1])) {
            Template t = Templates.warningNotFound(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Member warned = AdminHandler.removeWarnings(guild, args[1]);

        Template t = Templates.warningsRemovedSuccess(args[0], warned);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

}
