package alloy.command.fun;

import alloy.command.util.AbstractCommand;
import alloy.handler.SayHandler;
import alloy.input.discord.AlloyInputData;
import alloy.main.Sendable;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class SayCommand extends AbstractCommand {

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
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);
        Message msg = data.getMessageActual();

        if ( SayHandler.isWhitelisted(m) )
            SayHandler.sayRaw(channel , bot , msg);
        else if ( DisPermUtil.checkPermission(m , getPermission() ))
            SayHandler.sayAdmin(channel , bot , msg);
        
    }
    
}
