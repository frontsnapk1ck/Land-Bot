package alloy.command.administration;

import java.util.List;

import alloy.command.util.AbstractCommand;
import alloy.gameobjects.Warning;
import alloy.handler.WarningHandeler;
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


public class WarningsCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() 
    {
        return DisPerm.MOD;
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild guild = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = guild.getMember(author);
        


        if (!DisPermUtil.checkPermission(m, getPermission()))
        {
            Template t = Templates.noPermission(getPermission() , author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WarningsCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if ( args.length < 1 )
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setFrom("WarningsCommand");
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Member target = DisUtil.findMember(guild, args[0] );

        if (target == null)
        {
            Template t = Templates.userNotFound( args[0] );
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WarningsCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Long id = target.getIdLong();
        List<Warning> warnings = WarningHandeler.getWanrings( guild , id );
        String warningString = WarningHandeler.makeWaringTable(warnings);
        Template t = Templates.warnings(warningString);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("WarningsCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);

    }
    
    

}
