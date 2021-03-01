package alloy.command.fun;

import java.util.function.Consumer;

import alloy.command.util.AbstractCooldownCommand;
import alloy.handler.FunChatHandler;
import alloy.input.discord.AlloyInputData;
import alloy.main.Alloy;
import alloy.main.intefs.Queueable;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.main.intefs.handler.CooldownHandler;
import disterface.util.template.Template;
import alloy.templates.Templates;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;

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

        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>() 
        {
            @Override
            public void accept(ErrorResponseException t) 
            {
                Alloy.LOGGER.warn("KickCommand", t.getMessage());
            }

            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler eHandler = new ErrorHandler().handle(ErrorResponse.UNKNOWN_USER, consumer);

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

        msg.delete().queue(null , eHandler);
        
        addUserCooldown(m, g, handler, getCooldownTime(g) , q);

    }
    
}
