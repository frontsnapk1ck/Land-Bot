package alloy.handler;

import java.util.List;

import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SayHandler {

    public static boolean isWhitelisted(Member m) {
        List<Long> whitelisted = AlloyUtil.getWhitelisted();
        for (Long id : whitelisted) {
            if (id == m.getIdLong())
                return true;
        }
        return false;
    }

    public static void sayRaw(TextChannel channel, Sendable bot, Message msg) {
        String out = msg.getContentRaw().toString().substring(5);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("SayHandler");
        sm.setMessage(out);
        bot.send(sm);
        msg.delete().queue();
    }

    public static void sayAdmin(TextChannel channel, Sendable bot, Message msg) {
        String out = msg.getContentRaw().toString().substring(5);
        Template t = Templates.sayAdmin(out, msg);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("SayHandler");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        msg.delete().queue();
    }

}
