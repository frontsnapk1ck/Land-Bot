package alloy.command.configuration;

import alloy.command.util.AbstractCommand;
import alloy.handler.BlacklistHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.DisUtil;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class XPBlacklistCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() 
    {
        return DisPerm.ADMINISTRATOR;
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);

        if (!DisPermUtil.checkPermission( m , getPermission()))
        {
            Template t = Templates.noPermission(getPermission() , author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("BlacklistCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);  
            return;
        }

        boolean view =  args.length == 0 ||
                        args[0].equalsIgnoreCase("view");
        if (view)
            BlacklistHandler.view(g , channel , bot);
        
        if ( args.length != 2 )
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setFrom("BlacklistCommand");
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args[0].equalsIgnoreCase("add"))
            add( args[1] , bot , channel , g );
        else if (args[0].equalsIgnoreCase("remove"))
            remove( args[1] , bot , channel , g );

    }

    private void add (String c, Sendable bot, TextChannel channel, Guild g)
    {
        if (!DisUtil.isValidChannel(g , c))
        {
            Template t = Templates.invalidChannel( c );
            SendableMessage sm = new SendableMessage();
            sm.setFrom("BlacklistCommand");
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (BlacklistHandler.isBlacklisted(g, channel.getAsMention()))
        {
            Template t = Templates.channelisAlreadyBlacklisted(channel);
            SendableMessage sm = new SendableMessage();
            sm.setFrom("BlacklistCommand");
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        BlacklistHandler.add( g, c);

        Template t = Templates.blackListAddSucsess( c );
        SendableMessage sm = new SendableMessage();
        sm.setFrom("BlacklistCommand");
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        bot.send(sm);

        BlacklistHandler.view(g, channel, bot);
    }

    private void remove (String c, Sendable bot, TextChannel channel, Guild g)
    {
        if (!DisUtil.isValidChannel(g , c))
        {
            Template t = Templates.invalidChannel( c );
            SendableMessage sm = new SendableMessage();
            sm.setFrom("BlacklistCommand");
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
        
        if (!BlacklistHandler.isBlacklisted(g , c))
        {
            Template t = Templates.channelIsNotBlacklisted( c );
            SendableMessage sm = new SendableMessage();
            sm.setFrom("BlacklistCommand");
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        BlacklistHandler.remove(g, c);
        Template t = Templates.blackListRemoveSucsess( c );
        SendableMessage sm = new SendableMessage();
        sm.setFrom("BlacklistCommand");
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        bot.send(sm);

        BlacklistHandler.view(g, channel, bot);
    }
}
