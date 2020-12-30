package alloy.command.level;

import java.util.List;

import alloy.command.util.AbstractCommand;
import alloy.gameobjects.RankUp;
import alloy.gameobjects.player.Rank;
import alloy.handler.RankHandeler;
import alloy.handler.RankupHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.Util;

public class RankupCommand extends AbstractCommand {

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
            sm.setFrom("RankupCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if ( args.length < 1 )
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setFrom("RankupCommand");
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args[0].equalsIgnoreCase("add"))
            add( args , bot , channel , g );
        else if (args[0].equalsIgnoreCase("remove"))
            remove( args , bot , channel , g );
        else if (args[0].equalsIgnoreCase("view"))
            view( args , bot , channel , g );
        else if (args[0].equalsIgnoreCase("test"))
            test( args , bot , channel , g , m );
    }

    private void add (String[] args, Sendable bot, TextChannel channel, Guild g)
    {
        if (args.length < 3)
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage() );
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RankupCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if ( RankupHandler.hasRole(args , g) )
            RankupHandler.addRankupRole( args, bot , channel , g );
        else
            RankupHandler.addRankup(args, bot, channel, g);

        view(args, bot, channel, g);
    }

    private void remove (String[] args, Sendable bot, TextChannel channel, Guild g)
    {
        if (args.length < 2)
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage() );
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RankupCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!Util.validInt(args[1]))
        {
            Template t = Templates.invalidNumberFormat(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RankupCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        int level = Integer.parseInt(args[1]);

        if (!RankupHandler.containsLevel( g , level))
        {
            Template t = Templates.levelNotFound(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RankupCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        RankupHandler.removeRankup(level , g , channel , bot);
        view(args, bot, channel, g);
    }

    private void view (String[] args, Sendable bot, TextChannel channel, Guild g)
    {
        List<RankUp> rus = RankupHandler.loadRankups( g );
        Template t = Templates.viewRankUps(rus);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("RankupHandler");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        return;
    }

    private void test (String[] args, Sendable bot, TextChannel channel, Guild g , Member m)
    {
        if (args.length < 2)
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage() );
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RankupCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!Util.validInt(args[1]))
        {
            Template t = Templates.invalidNumberFormat(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RankupCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        int level = Integer.parseInt(args[1]);
        Rank r = RankupHandler.getRank( g , level);
        
        RankHandeler.anounceRankUp( r , m , false, bot , channel);

    }
    
}
