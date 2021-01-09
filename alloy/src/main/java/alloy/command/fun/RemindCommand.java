package alloy.command.fun;

import alloy.command.util.AbstractCommand;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Queueable;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.job.jobs.RemindJob;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.StringUtil;
import utility.time.TimeUtil;

public class RemindCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        User author = data.getUser();
        Sendable bot = data.getSendable();
        String[] args = AlloyInputUtil.getArgs(data);
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);
        Queueable q = data.getQueue();

        if (args.length < 1)
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage() );
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RemindCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return; 
        }

        long delay = TimeUtil.toMillis(args[0]);

        if (delay == 0l)
        {
            Template t = Templates.timeNotRecogocnized( args[0] );
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RemindCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return; 
        }

        String out = StringUtil.joinStrings(args, 1);
        
        Template template = Templates.remindCard(args[0] , out );

        Template t = Templates.remindMe( out );
        Message outM = new MessageBuilder()
                            .setEmbed(t.getEmbed())
                            .append(m.getAsMention())
                            .build();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("RemindCommand {...} RemindJob");
        sm.setMessage(outM);

        RemindJob job = new RemindJob(bot, sm);
        q.queueIn(job, delay);

        SendableMessage sm2 = new SendableMessage();
        sm2.setChannel(channel);
        sm2.setFrom("RemindCommand");
        sm2.setMessage(template.getEmbed());
        bot.send(sm2);
    }
    
}
