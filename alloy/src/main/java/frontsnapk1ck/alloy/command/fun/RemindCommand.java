package frontsnapk1ck.alloy.command.fun;

import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Queueable;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.job.jobs.RemindJob;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import frontsnapk1ck.utility.StringUtil;
import frontsnapk1ck.utility.time.TimeUtil;

public class RemindCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) {
        Sendable bot = data.getSendable();
        String[] args = AlloyInputUtil.getArgs(data);
        TextChannel channel = data.getChannel();

        if (args.length < 1) {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args[0].equalsIgnoreCase("dm"))
            remindDM(data);
        else
            remindNormal(data);
    }

    private void remindNormal(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        Sendable bot = data.getSendable();
        String[] args = AlloyInputUtil.getArgs(data);
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);
        Queueable q = data.getQueue();

        long delay = TimeUtil.toMillis(args[0]);

        if (delay == 0l) {
            AlloyTemplate t = Templates.timeNotRecognized(args[0]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String out = StringUtil.joinStrings(args, 1);

        AlloyTemplate template = Templates.remindCard(args[0], out);

        AlloyTemplate t = Templates.remindMe(out);
        Message outM = new MessageBuilder().setEmbeds(t.getEmbed()).append(m.getAsMention()).build();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(outM);

        RemindJob job = new RemindJob(bot, sm);
        q.queueIn(job, delay);

        SendableMessage sm2 = new SendableMessage();
        sm2.setChannel(channel);
        sm2.setFrom(getClass());
        sm2.setMessage(template.getEmbed());
        bot.send(sm2);
    }

    private void remindDM(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        Sendable bot = data.getSendable();
        String[] args = AlloyInputUtil.getArgs(data);
        TextChannel channel = data.getChannel();
        Message msg = data.getMessageActual();
        Member m = g.getMember(author);
        Queueable q = data.getQueue();

        if (args.length < 2) {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        long delay = TimeUtil.toMillis(args[1]);

        if (delay == 0l) {
            AlloyTemplate t = Templates.timeNotRecognized(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        PrivateChannel pc = m.getUser().openPrivateChannel().complete();
        if (pc == null) {
            AlloyTemplate t = Templates.privateMessageFailed(m);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String out = StringUtil.joinStrings(args, 2);

        AlloyTemplate template = Templates.remindCard(args[1], out);

        AlloyTemplate t = Templates.remindMeDM(out, msg);
        Message outM = new MessageBuilder().setEmbeds(t.getEmbed()).append(m.getAsMention()).build();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(pc);
        sm.setFrom(getClass());
        sm.setMessage(outM);

        RemindJob job = new RemindJob(bot, sm);
        q.queueIn(job, delay);

        SendableMessage sm2 = new SendableMessage();
        sm2.setChannel(channel);
        sm2.setFrom(getClass());
        sm2.setMessage(template.getEmbed());
        bot.send(sm2);

    }

}
