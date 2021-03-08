package alloy.command.voice;

import java.util.List;

import alloy.command.util.AbstractCommand;
import alloy.handler.command.VoiceHandler;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.templates.Templates;
import disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class JoinCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);

        List<VoiceChannel> vcs = g.getVoiceChannels();
        for (VoiceChannel vc : vcs) 
        {
            if (VoiceHandler.memberIn(vc, m)) 
            {
                joinVc(vc, bot, channel);
                return;
            }
        }

        Template t = Templates.voiceMemberNotInChannel(m);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("JoinCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

    private void joinVc(VoiceChannel vc, Sendable bot, TextChannel channel) {
        if (VoiceHandler.join(vc)) {
            Template t = Templates.voiceJoinSuccess(vc);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("JoinCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        } else {
            Template t = Templates.voiceJoinFail(vc);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("JoinCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }
    }

}
