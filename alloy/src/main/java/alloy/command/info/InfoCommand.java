package alloy.command.info;

import alloy.command.util.AbstractCommand;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.DisUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class InfoCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (args.length == 0)
        {
            self(bot, channel);
            return;
        }

        else if (args[0].equalsIgnoreCase("server"))
        {
            server(g, bot, channel);
            return;
        }

        if (args.length != 2) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            sm.setFrom("InfoCommand");
            bot.send(sm);
            return;
        }

        if (args[0].equalsIgnoreCase("user"))
            user(args[1], bot, channel);

        else if (args[0].equalsIgnoreCase("role"))
            role(args[1], bot, channel);
    }

    private void self(Sendable bot, TextChannel channel) {
        Template t = Templates.infoSelf();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom("InfoCommand");
        bot.send(sm);
    }

    private void user(String user, Sendable bot, TextChannel channel)
    {
        Guild g = channel.getGuild();
        User u = DisUtil.parseUser(user);
        Member m = g.getMember(u);

        Member target = DisUtil.findMember(g, user );

        if (target == null)
        {
            Template t = Templates.userNotFound( user );
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WarnCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
        
        Template t = Templates.infoUser(m);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom("InfoCommand");
        bot.send(sm);
    }

    private void server (Guild g, Sendable bot, TextChannel channel)
    {
        Template t = Templates.infoServer(g);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom("InfoCommand");
        bot.send(sm);
    }

    private void role (String role, Sendable bot, TextChannel channel)
    {
        Guild g = channel.getGuild();
        Role r = DisUtil.parseRole( role , g );

        if (r == null)
        {
            Template t = Templates.roleNotFound(role);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            sm.setFrom("InfoCommand");
            bot.send(sm);
        }

        Template t = Templates.infoRole(r);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom("InfoCommand");
        bot.send(sm);
    }
    
}
