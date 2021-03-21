package frontsnapk1ck.alloy.command.voice;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import frontsnapk1ck.alloy.audio.GuildMusicManager;
import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Audible;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class NowPlayingCommand extends AbstractCommand {


    @Override
    public void execute(AlloyInputData data) 
    {
        TextChannel channel = data.getChannel();
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        Audible audible = data.getAudible();

        GuildMusicManager musicManager = audible.getGuildAudioPlayer(g);
        AudioTrack nowPlaying = musicManager.getPlayer().getPlayingTrack();

        Template t = Templates.nowPlaying(nowPlaying);
        SendableMessage sm = new SendableMessage();
        sm.setFrom(getClass());
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }
    
}
