package frontsnapk1ck.alloy.command.voice;

import java.util.List;
import java.util.function.Consumer;

import com.github.connyscode.ctils.jTrack.Song;
import com.github.connyscode.ctils.jTrack.backend.SongNotFoundException;
import com.github.connyscode.ctils.jTrack.backend.types.SongSearchResult;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import frontsnapk1ck.alloy.audio.lyric.AlloyjLyricClient;
import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Audible;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.job.jobs.DelayJob;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import frontsnapk1ck.io.FileReader;
import frontsnapk1ck.utility.StringUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class LyricsCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        Consumer<AlloyInputData> consumer = new Consumer<AlloyInputData>()
        {
            @Override
            public void accept(AlloyInputData t) 
            {
                searchImp(t);
            }    
        };

        DelayJob<AlloyInputData> j = new DelayJob<AlloyInputData>(consumer, data);
        data.getQueue().queue(j);
    }

    protected void searchImp(AlloyInputData data) 
    {
        String[] args = AlloyInputUtil.getArgs(data);

        if (args.length == 0)
            currentSong(data);
        else
            searchSong(args , data);
    }

    private void currentSong(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        TextChannel channel = data.getChannel();
        Audible audible = data.getAudible();
        Sendable bot = data.getSendable();

        AudioTrack track = audible.getGuildAudioPlayer(g).getPlayer().getPlayingTrack();
        if (track == null)
        {
            AlloyTemplate t = Templates.noMusicPlaying();
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
        String name = track.getInfo().title;
        showLyrics(data, name);
    }

    private void searchSong(String[] args, AlloyInputData data) 
    {
        String name = StringUtil.joinStrings(args);
        
        showLyrics(data, name);
    }

    private void showLyrics(AlloyInputData data, String name) 
    {
        TextChannel channel = data.getChannel();
        Sendable bot = data.getSendable();

        int i = name.indexOf("(");
        name = name.substring(0,i).trim();
        String key = FileReader.read(AlloyUtil.GENIUS_KEY)[0];

        AlloyjLyricClient client = new AlloyjLyricClient(key);
        
        List<SongSearchResult> searchResults = client.performSongSearch(name);
        if (searchResults == null || searchResults.size() == 0)
        {
            AlloyTemplate t = Templates.noLyricsFound(name);
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        try 
        {
            Song song = client.getSong(searchResults.get(0));
            send(data , song);
        }
        catch (SongNotFoundException e) 
        {
            AlloyTemplate t = Templates.noLyricsFound(name);
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
    }

    private void send(AlloyInputData data, Song song) 
    {
        TextChannel channel = data.getChannel();
        Sendable bot = data.getSendable();
        
        AlloyTemplate t = Templates.songLyrics(song);

        try 
        {
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }
        catch (IllegalArgumentException e) 
        {
            List<MessageEmbed> embeds = Templates.getEmbeds(t);
            for (MessageEmbed messageEmbed : embeds) 
            {
                SendableMessage sm = new SendableMessage();
                sm.setFrom(getClass());
                sm.setChannel(channel);
                sm.setMessage(messageEmbed);
                bot.send(sm);
            }
        }
    }
    
}
