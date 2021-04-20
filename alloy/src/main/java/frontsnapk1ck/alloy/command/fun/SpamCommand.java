package frontsnapk1ck.alloy.command.fun;

import frontsnapk1ck.alloy.command.util.AbstractCooldownCommand;
import frontsnapk1ck.alloy.handler.command.FunHandler;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Queueable;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.intefs.handler.CooldownHandler;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class SpamCommand extends AbstractCooldownCommand {

    @Override
    public void execute(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        CooldownHandler handler = data.getCooldownHandler();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);
        Queueable queueable = data.getQueue();

        if (userOnCooldown(author, g, handler)) {
            AlloyTemplate t = Templates.onCooldown(m);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (FunHandler.isStart(args)) 
        {
            MessageEmbed embed = startSpam(channel, args, author, queueable , bot);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(embed);
            bot.send(sm);
            return;
        }

        else if (FunHandler.isStop(args)) {
            MessageEmbed embed = stopSpam(args);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(embed);
            bot.send(sm);
            return;
        }

        AlloyTemplate t = Templates.argumentsNotRecognized(data.getMessageActual());
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

    private MessageEmbed stopSpam(String[] args) {
        Long id = 0l;
        try {
            id = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            AlloyTemplate t = Templates.invalidNumberFormat(args);
            return t.getEmbed();
        }

        if (FunHandler.stopSpam(id)) {
            AlloyTemplate t = Templates.spamRunnableStopped(id);
            return t.getEmbed();
        } else {
            AlloyTemplate t = Templates.spamRunnableIdNotFound(id);
            return t.getEmbed();
        }
    }

    private MessageEmbed startSpam(TextChannel chan, String[] args, User author, Queueable queueable, Sendable bot) 
    {
        if (!FunHandler.validCommand(args)) 
        {
            AlloyTemplate temp = Templates.invalidNumberFormat(args);
            return temp.getEmbed();
        }

        Long id = FunHandler.makeRunnable(chan.getGuild(), args, author, bot, queueable).getID();

        AlloyTemplate temp = Templates.spamRunnableCreated(id);
        return temp.getEmbed();
    }

}
