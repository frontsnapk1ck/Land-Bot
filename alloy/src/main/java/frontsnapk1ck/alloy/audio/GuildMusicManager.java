package frontsnapk1ck.alloy.audio;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.utility.cache.Cacheable;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager implements TrackListener , Cacheable {

    /**
     * Audio player for the guild.
     */
    private final AudioPlayer player;
    
    /**
     * Track scheduler for the player.
     */
    private final TrackScheduler scheduler;

    /**
     * list of users who have voted to skip the current song
     */
    private List<User> skipped;

    private Guild guild;

    /**
     * Creates a player and a track scheduler.
     * @param manager Audio player manager to use for creating the player.
     * @param g the guild that this player is managing
     */
    public GuildMusicManager(AudioPlayerManager manager, Guild g) 
    {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
        scheduler.addListener(this);
        this.skipped = new ArrayList<User>();
        this.guild = g;
    }

    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    public AudioPlayerSendHandler getSendHandler() 
    {
        return new AudioPlayerSendHandler(player);
    }

    /**
     * 
     * @return the {@link AudioPlayer} for this guild
     */
    public AudioPlayer getPlayer() 
    {
        return player;
    }

    /**
     * 
     * @return the {@link TrackScheduler} for this guild
     */
    public TrackScheduler getScheduler() 
    {
        return scheduler;
    }

    /**
     * 
     * @return the {@link List} of {@link User}s who have voted to skip the song in this guild
     */
    public List<User> getSkipped() 
    {
        return skipped;
    }

    /**
     * 
     * @param u the user that has requested to skip the song
     * @return {@code true} if the user has been added and {@code false} if the user was not added
     */
    public boolean addSkipped(User u)
    {
        if (this.skipped.contains(u))
            return false;
        this.skipped.add(u);
        return true;
    }

    /**
     * skips the current song and resets the list of users who voted to skip
     */
    public void nextTrack() 
    {
        this.scheduler.nextTrack();
        this.skipped.clear();
    }

    @Override
    public void onTrackEnd() 
    {
        this.skipped.clear();
        AlloyUtil.audioStopped(this);
    }

    @Override
    public GuildMusicManager getData() 
    {
        return this;
    }

    public Guild getGuild() 
    {
        return guild;
    }

    @Override
    public void onTrackStart() 
    {
        AlloyUtil.audioStarted(this);
    }
}