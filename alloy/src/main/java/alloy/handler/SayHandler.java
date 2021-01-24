package alloy.handler;

import java.util.List;
import java.util.function.Consumer;

import alloy.main.Alloy;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class SayHandler {

    public static boolean isWhitelisted(Member m) {
        List<Long> whitelisted = AlloyUtil.getWhitelisted();
        for (Long id : whitelisted) {
            if (id == m.getIdLong())
                return true;
        }
        return false;
    }

    public static void sayRaw(TextChannel channel, Sendable bot, Message msg) 
    {
        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>() 
        {
            @Override
            public void accept(ErrorResponseException t) 
            {
                Alloy.LOGGER.warn("DeleteMessageJob", t.getMessage());
            }

            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE, consumer);

        String out = msg.getContentRaw().toString().substring(5);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("SayHandler");
        sm.setMessage(out);
        bot.send(sm);
        msg.delete().queue(null , handler);
    }

    public static void sayAdmin(TextChannel channel, Sendable bot, Message msg) 
    {
        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>() 
        {
            @Override
            public void accept(ErrorResponseException t) 
            {
                Alloy.LOGGER.warn("DeleteMessageJob", t.getMessage());
            }

            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE, consumer);

        String out = msg.getContentRaw().toString().substring(5);
        Template t = Templates.sayAdmin(out, msg);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("SayHandler");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        msg.delete().queue(null, handler);
    }

}
