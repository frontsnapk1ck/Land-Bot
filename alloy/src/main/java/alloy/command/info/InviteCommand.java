package alloy.command.info;

import alloy.command.util.AbstractCooldownCommand;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Queueable;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.job.jobs.InviteJob;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.event.Job;

public class InviteCommand extends AbstractCooldownCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        User author = data.getUser();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);
        Queueable q = data.getQueue();

        Template t = Templates.invite();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom("InviteCommand");
        bot.send(sm);

        Job j = new InviteJob(m, bot);
        q.queue(j);

    }
    
}
