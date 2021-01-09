package alloy.command.info;

import java.util.ArrayList;
import java.util.List;

import alloy.command.util.AbstractCooldownCommand;
import alloy.handler.HelpHandler;
import alloy.input.discord.AlloyInputData;
import alloy.main.Queueable;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.main.handler.CooldownHandler;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.job.jobs.HelpJob;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.event.Job;

public class HelpCommand extends AbstractCooldownCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        User author = data.getUser();
        Sendable bot = data.getSendable();
        CooldownHandler handler = data.getCooldownHandler();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);
        Queueable q = data.getQueue();
        
        if (userOnCooldown(author, g, handler))
        {
            Template t = Templates.onCooldown(m);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("HelpCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);  
            return;
        }

        List<String> files = HelpHandler.loadHelpFiles();
        List<MessageEmbed> messages = new ArrayList<MessageEmbed>();

        for (String path : files)
            messages.add(HelpHandler.loadEmbed(path));
        
        Job j = new HelpJob(m.getUser(), messages, bot);
        q.queue(j);

        Template t = Templates.helpSentTemplate();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("HelpCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

    
}
