package alloy.command.fun;

import alloy.command.util.AbstractCooldownCommand;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Queueable;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.main.intefs.handler.CooldownHandler;
import disterface.util.template.Template;
import alloy.templates.Templates;
import alloy.utility.discord.DisUtil;
import alloy.utility.discord.perm.AlloyPerm;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import alloy.utility.job.jobs.MessageEditJob;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class HackCommand extends AbstractCooldownCommand {

    @Override
    public DisPerm getPermission() 
    {
        return AlloyPerm.CREATOR;
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        User author = data.getUser();
        Sendable bot = data.getSendable();
        CooldownHandler handler = data.getCooldownHandler();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);
        String[] args = AlloyInputUtil.getArgs(data);
        Queueable q = data.getQueue();

        if (!DisPermUtil.checkPermission(m, getPermission())) {
            Template t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

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

        if (args.length < 1)
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
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

        Message message = new MessageBuilder("HACKING\t**" +  target.getUser().getAsTag() + "**").build();
        Message e1 = new MessageBuilder("HACKING\t**" +  target.getUser().getAsTag() + "**\t.").build();
        Message e2 = new MessageBuilder("HACKING\t**" +  target.getUser().getAsTag() + "**\t..").build();
        Message e3 = new MessageBuilder("HACKING\t**" +  target.getUser().getAsTag() + "**\t...").build();

        int num1 = (int)(Math.random()*255);
        int num2 = (int)(Math.random()*254) + 1;

        Message e4 = new MessageBuilder("HACKED\t**" +  target.getUser().getAsTag() + "**\n their ip is `192.168." + num1 + "." + num2 + "`").build();

        SendableMessage sm = new SendableMessage();
        sm.setMessage(message);
        sm.setChannel(channel);
        sm.setFrom(getClass());
        MessageAction action = bot.getAction(sm);
        if (action == null)
            return;
        Message toEdit = action.complete();

        MessageEditJob mej1 = new MessageEditJob(toEdit, e1);
        MessageEditJob mej2 = new MessageEditJob(toEdit, e2);
        MessageEditJob mej3 = new MessageEditJob(toEdit, e3);
        MessageEditJob mej4 = new MessageEditJob(toEdit, e4);
        
        addUserCooldown(m, g, handler, getCooldownTime(g) , q);

        q.queueIn(mej1, 750l);
        q.queueIn(mej2, 1500l);
        q.queueIn(mej3, 2250l);
        q.queueIn(mej4, 3000l);
    }
    
}
