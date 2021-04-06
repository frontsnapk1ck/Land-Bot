package frontsnapk1ck.alloy.command.info;

import java.util.function.Consumer;

import frontsnapk1ck.alloy.command.util.AbstractCooldownCommand;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Queueable;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.intefs.handler.CooldownHandler;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.job.jobs.DelayJob;
import frontsnapk1ck.disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class PingCommand extends AbstractCooldownCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        DelayJob<AlloyInputData> j = new DelayJob<AlloyInputData>(getConsumer(), data);
        data.getQueue().queue(j);

    }

    private Consumer<? super AlloyInputData> getConsumer() 
    {
        return new Consumer<AlloyInputData>()
        {
            @Override
            public void accept(AlloyInputData t) 
            {
                executeImp(t);
            }
            
        };
    }

    public void executeImp (AlloyInputData data)
    {
        User author = data.getUser();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Queueable q = data.getQueue();
        Guild g = data.getGuild();
        CooldownHandler handler = data.getCooldownHandler();
        Member m = g.getMember(author);

        if (userOnCooldown(author, g, handler)) 
        {
            Template t = Templates.onCooldown(m);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
     
        long start = System.currentTimeMillis();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage("checking ping");
        MessageAction action = bot.getAction( sm );
        if (action != null)
        {
            Message message = action.complete();
            message.editMessage("ping is " + (System.currentTimeMillis() - start) + "ms").queue();
        }

        addUserCooldown(m, g, handler, getCooldownTime(g), q);

    }

}
