package frontsnapk1ck.alloy.command.voice;

import java.util.function.Consumer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import frontsnapk1ck.alloy.audio.GuildMusicManager;
import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.handler.command.AudioHandler;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Audible;
import frontsnapk1ck.alloy.main.intefs.Queueable;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.job.jobs.DelayJob;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import frontsnapk1ck.utility.StringUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class PlayCommand extends AbstractCommand  {

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Audible audible = data.getAudible();

        GuildMusicManager musicManager = audible.getGuildAudioPlayer(g);
        AudioPlayerManager playerManager = audible.getPlayerManager();

        String[] args = AlloyInputUtil.getArgs(data);

        if (args.length == 0)
        {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String trackUrl = AudioHandler.getUrl(args);

        if (trackUrl == null)
        {
            AlloyTemplate t = Templates.songNotFound(StringUtil.joinStrings(args));
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() 
        {
            @Override
            public void trackLoaded(AudioTrack track) 
            {
                if (play(data, musicManager, track))
                {   
                    AlloyTemplate t = Templates.addedToMusicQueue(track);
                    SendableMessage sm = new SendableMessage();
                    sm.setFrom(getClass());
                    sm.setChannel(channel);
                    sm.setMessage(t.getEmbed());
                    bot.send(sm);
                }
        
            }
        
            @Override
            public void playlistLoaded(AudioPlaylist playlist) 
            {
                AudioTrack firstTrack = playlist.getSelectedTrack();
        
                if (firstTrack == null)
                    firstTrack = playlist.getTracks().get(0);

                if (play(data, musicManager, firstTrack))
                {
             
                    AlloyTemplate t = Templates.addedToMusicQueue(playlist);
                    SendableMessage sm = new SendableMessage();
                    sm.setFrom(getClass());
                    sm.setChannel(channel);
                    sm.setMessage(t.getEmbed());
                    bot.send(sm);
                }
            }
        
            @Override
            public void noMatches() 
            {
                AlloyTemplate t = Templates.notingFoundBy(args);
                SendableMessage sm = new SendableMessage();
                sm.setFrom(getClass());
                sm.setChannel(channel);
                sm.setMessage(t.getEmbed());
                bot.send(sm);
            }
        
            @Override
            public void loadFailed(FriendlyException exception) 
            {
                AlloyTemplate t = Templates.couldNotPlay(exception);
                SendableMessage sm = new SendableMessage();
                sm.setFrom(getClass());
                sm.setChannel(channel);
                sm.setMessage(t.getEmbed());
                bot.send(sm);
            }
        });
      }

    private boolean play(AlloyInputData data, GuildMusicManager musicManager, AudioTrack track)
    {
        Queueable q = data.getQueue();
        Guild g = data.getGuild();

        if ( AudioHandler.isConnected(g))
        {
            musicManager.getScheduler().queue(track);
            return true;
        }
        else
        {
            JoinCommand command = new JoinCommand();
            command.execute(data);

            Consumer<AudioTrack> consumer = new Consumer<AudioTrack>()
            {
                @Override
                public void accept(AudioTrack t) 
                {
                    musicManager.getScheduler().queue(t);
                }
            };

            if (!AudioHandler.isConnected(g))
                return false;
            
            DelayJob<AudioTrack> j = new DelayJob<AudioTrack>(consumer , track);
            q.queueIn(j, 2000L );
            return true;
        }
    }
}
