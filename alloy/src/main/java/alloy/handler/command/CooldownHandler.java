package alloy.handler.command;

import alloy.gameobjects.Server;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import disterface.util.template.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class CooldownHandler {

    public static void setXpCooldown(Guild g, int newTime) {
        Server s = AlloyUtil.loadServer(g);
        s.changeXPCooldown(newTime);
    }

    public static void setCooldown(Guild g, int newTime) {
        Server s = AlloyUtil.loadServer(g);
        s.changeCooldown(newTime);
    }

    public static void showXPCooldown(AlloyInputData data) {
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Guild g = data.getGuild();

        Server s = AlloyUtil.loadServer(g);
        int cooldown = s.getXPCooldown();

        Template t = Templates.showXPCooldown(cooldown);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("CooldownHandler");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

    public static void showCooldown(AlloyInputData data) {
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Guild g = data.getGuild();

        Server s = AlloyUtil.loadServer(g);
        int cooldown = s.getWorkCooldown();

        Template t = Templates.showCooldown(cooldown);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("CooldownHandler");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

    public static boolean isOnCooldown(Member m) {
        return false;
    }

}
