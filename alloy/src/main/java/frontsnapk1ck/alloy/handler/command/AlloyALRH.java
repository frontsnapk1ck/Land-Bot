package frontsnapk1ck.alloy.handler.command;

import java.util.function.Consumer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import frontsnapk1ck.alloy.audio.GuildMusicManager;
import frontsnapk1ck.alloy.command.voice.JoinCommand;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Queueable;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.job.jobs.AlloyDelayJob;
import frontsnapk1ck.utility.event.Result;
import frontsnapk1ck.utility.event.SimpleResult;
import frontsnapk1ck.utility.event.annotation.RequiredJob;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

@RequiredJob
public class AlloyALRH implements AudioLoadResultHandler {

    private AlloyInputData data;
    private Guild guild;
    private GuildMusicManager musicManager;
    private TextChannel channel;
    private Sendable bot;

    public AlloyALRH(AlloyInputData data)
    {
        this.data = data;
        this.guild = this.data.getGuild();
        this.musicManager = this.data.getAudible().getGuildAudioPlayer(this.guild);
        this.bot = this.data.getSendable();
        this.channel = this.data.getChannel();
    }

    @Override
    public void trackLoaded(AudioTrack track) 
    {
        try
        {
            if (play(data, musicManager, track).get().booleanValue())
            {   
                AlloyTemplate t = Templates.addedToMusicQueue(track);
                SendableMessage sm = new SendableMessage();
                sm.setFrom(getClass());
                sm.setChannel(channel);
                sm.setMessage(t.getEmbed());
                bot.send(sm);
            }    
        }
        catch (Exception exception)
        {
            AlloyTemplate t = Templates.couldNotPlay(exception);
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

        try
        {
            if (play(data, musicManager, firstTrack).get().booleanValue())
            {
                AlloyTemplate t = Templates.addedToMusicQueue(playlist);
                SendableMessage sm = new SendableMessage();
                sm.setFrom(getClass());
                sm.setChannel(channel);
                sm.setMessage(t.getEmbed());
                bot.send(sm);
            }

        }
        catch (Exception exception)
        {
            AlloyTemplate t = Templates.couldNotPlay(exception);
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
        AlloyTemplate t = Templates.notingFoundBy(AlloyInputUtil.getArgs(this.data));
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

    private Result<Boolean> play(AlloyInputData data, GuildMusicManager musicManager, AudioTrack track)
    {
        Queueable q = data.getQueue();
        Guild g = data.getGuild();

        SimpleResult<Boolean> result = new SimpleResult<Boolean>();

        if ( AudioHandler.isConnected(g))
        {
            musicManager.getScheduler().queue(track);
            result.finish(true);
            return result;
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
                    boolean connected = AudioHandler.isConnected(g);
                    if (connected)
                        musicManager.getScheduler().queue(t);
                    
                    result.finish(connected);
                }
            };
            
            AlloyDelayJob<AudioTrack> j = new AlloyDelayJob<AudioTrack>(consumer , track);
            q.queueIn(j, 2000L );
            return result;
        }
    }
    
}
