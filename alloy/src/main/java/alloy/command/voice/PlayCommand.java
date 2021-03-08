package alloy.command.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import alloy.audio.GuildMusicManager;
import alloy.command.util.AbstractCommand;
import alloy.handler.command.VoiceHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Audible;
import alloy.main.intefs.Queueable;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.templates.Templates;
import alloy.utility.discord.perm.DisPermUtil;
import alloy.utility.job.jobs.DelayJob;
import disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class PlayCommand extends AbstractCommand  {

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        User author = data.getUser();
        TextChannel channel = data.getChannel();
        Audible audible = data.getAudible();
        Member m = g.getMember(author);

        GuildMusicManager musicManager = audible.getGuildAudioPlayer(g);
        AudioPlayerManager playerManager = audible.getPlayerManager();

        String[] args = AlloyInputUtil.getArgs(data);

        if (!DisPermUtil.checkPermission(m, getPermission())) 
        {
            Template t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WarningsCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args.length == 0)
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setFrom("WarningsCommand");
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String trackUrl = VoiceHandler.getUrl(args);

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() 
        {
            @Override
            public void trackLoaded(AudioTrack track) 
            {
                if (play(data, musicManager, track))
                {   
                    Template t = Templates.addedToMusicQueue(track);
                    SendableMessage sm = new SendableMessage();
                    sm.setFrom("WarningsCommand");
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
             
                    Template t = Templates.addedToMusicQueue(playlist);
                    SendableMessage sm = new SendableMessage();
                    sm.setFrom("WarningsCommand");
                    sm.setChannel(channel);
                    sm.setMessage(t.getEmbed());
                    bot.send(sm);
                }
            }
        
            @Override
            public void noMatches() 
            {
                Template t = Templates.notingFoundBy(args);
                SendableMessage sm = new SendableMessage();
                sm.setFrom("WarningsCommand");
                sm.setChannel(channel);
                sm.setMessage(t.getEmbed());
                bot.send(sm);
            }
        
            @Override
            public void loadFailed(FriendlyException exception) 
            {
                Template t = Templates.couldNotPlay(exception);
                SendableMessage sm = new SendableMessage();
                sm.setFrom("WarningsCommand");
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

        if ( VoiceHandler.isConnected(g))
        {
            musicManager.scheduler.queue(track);
            return true;
        }
        else
        {
            JoinCommand command = new JoinCommand();
            command.execute(data);
        // }
        // if ( VoiceHandler.isConnected(g))    
        // {
            DelayJob<AudioTrack> j = new DelayJob<AudioTrack>(musicManager.scheduler::queue , track);
            q.queueIn(j, 1500);
            return true;
        }
        // return false;
    }
}
