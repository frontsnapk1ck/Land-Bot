package alloy.handler.command.fun;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import alloy.main.Alloy;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import disterface.util.template.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
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

        
        if (msg.getAttachments().size() != 0)
            sendAttachments(msg,channel,bot);
        else 
            sendNormal(msg,channel,bot);
        msg.delete().queue(null , handler);
    }

    private static void sendAttachments(Message msg, TextChannel channel, Sendable bot) 
    {
        List<Attachment> attachments = msg.getAttachments();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(SayHandler.class);

        for (Attachment attachment : attachments) 
        {
            String path = AlloyUtil.TMP_FOLDER;
            path += msg.getChannel().getId() + "-" + msg.getId();
            path += "." + attachment.getFileExtension();
            File f = new File(path);

            try 
            {
                File down = attachment.downloadToFile(f).get();
                sm.addFile(down);
            }
            catch (InterruptedException | ExecutionException e1) 
            {
                Alloy.LOGGER.error("JDAEvents", e1);
            }
        }

        try {
            String out = msg.getContentRaw().toString().substring(5);
            sm.setMessage(out);    
        } catch (Exception e) 
        {
            sm.setMessage(" ");
        }
        bot.send(sm);
    }

    private static void sendNormal(Message msg, TextChannel channel, Sendable bot) 
    {
        if (msg.getContentRaw().length() < 5 )
            return;

        String out = msg.getContentRaw().toString().substring(5);

        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(SayHandler.class);
        sm.setMessage(out);
        bot.send(sm);
    }

    public static void sayAdmin(TextChannel channel, Sendable bot, Message msg) 
    {

        if (msg.getContentRaw().length() < 5 )
            return;
        
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
        sm.setFrom(SayHandler.class);
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        msg.delete().queue(null, handler);
    }

}
