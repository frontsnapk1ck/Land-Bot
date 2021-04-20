package frontsnapk1ck.alloy.command.fun;

import frontsnapk1ck.alloy.command.util.AbstractCooldownCommand;
import frontsnapk1ck.alloy.handler.command.FunHandler;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.main.intefs.Queueable;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.intefs.handler.CooldownHandler;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class DeadChatCommand extends AbstractCooldownCommand {

    @Override
    public long getCooldownTime(Guild g) 
    {
        return 15l;
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
        Message msg = data.getMessageActual();
        Queueable q = data.getQueue();

        if (userOnCooldown(author, g, handler))
        {
            AlloyTemplate t = Templates.onCooldown(m);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);  
            return;
        }

        String[] chats = FunHandler.getDeadChats();
        int i = (int) (Math.random() * chats.length);
        String message = chats[i];

        SendableMessage sm = new SendableMessage();
        sm.setMessage(message);
        sm.setChannel(channel);
        sm.setFrom(getClass());
        bot.send(sm);

        try 
        {
            msg.delete().complete();
        }
        catch (Exception e)
        {
            Alloy.LOGGER.warn(getClass().getSimpleName(), e.getMessage());
        }
        
        addUserCooldown(m, g, handler, getCooldownTime(g) , q);

    }
    
}
