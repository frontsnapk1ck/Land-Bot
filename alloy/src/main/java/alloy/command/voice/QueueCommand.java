package alloy.command.voice;

import java.util.concurrent.BlockingQueue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import alloy.audio.GuildMusicManager;
import alloy.command.util.AbstractCommand;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Audible;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.templates.Templates;
import disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class QueueCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        TextChannel channel = data.getChannel();
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        Audible audible = data.getAudible();

        GuildMusicManager musicManager = audible.getGuildAudioPlayer(g);
        AudioTrack nowPlaying = musicManager.player.getPlayingTrack();
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();

        Template t = Templates.musicQueue(queue,nowPlaying);
        SendableMessage sm = new SendableMessage();
        sm.setFrom(getClass());
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }
    
}
