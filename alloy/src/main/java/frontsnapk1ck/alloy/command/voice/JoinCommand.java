package frontsnapk1ck.alloy.command.voice;

import java.util.List;

import frontsnapk1ck.alloy.audio.GuildMusicManager;
import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.handler.command.AudioHandler;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Audible;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
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
        Audible a = data.getAudible();

        List<VoiceChannel> vcs = g.getVoiceChannels();
        for (VoiceChannel vc : vcs) 
        {
            if (AudioHandler.memberIn(vc, m)) 
            {
                joinVc(vc, bot, channel , a);
                return;
            }
        }

        AlloyTemplate t = Templates.voiceMemberNotInChannel(m);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

    private void joinVc(VoiceChannel vc, Sendable bot, TextChannel channel, Audible a) 
    {
        if (AudioHandler.join(vc)) 
        {
            AlloyTemplate t = Templates.voiceJoinSuccess(vc);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            GuildMusicManager gmm = a.getGuildAudioPlayer(channel.getGuild());
            AlloyUtil.audioStopped(gmm);
        }

        else
        {
            AlloyTemplate t = Templates.voiceJoinFail(vc);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }
    }

}
