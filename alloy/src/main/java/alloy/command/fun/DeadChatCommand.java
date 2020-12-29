package alloy.command.fun;

import alloy.command.util.AbstractCooldownCommand;
import alloy.handler.FunChatHandler;
import alloy.input.discord.AlloyInputData;
import alloy.main.Queueable;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.main.handler.CooldownHandler;
import alloy.templates.Template;
import alloy.templates.Templates;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
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
        Queueable q = data.getQueue();

        if (userOnCooldown(author, g, handler))
        {
            Template t = Templates.onCooldown(m);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("DeadChatCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);  
            return;
        }

        String[] chats = FunChatHandler.getDeadChats();
        int i = (int) (Math.random() * chats.length);
        String message = chats[i];

        SendableMessage sm = new SendableMessage();
        sm.setMessage(message);
        sm.setChannel(channel);
        sm.setFrom("DeadChatCommand");
        bot.send(sm);
        
        addUserCooldown(author, g, handler, getCooldownTime(g) , q);

    }
    
}
