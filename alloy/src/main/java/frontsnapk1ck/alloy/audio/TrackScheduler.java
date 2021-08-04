package frontsnapk1ck.alloy.audio;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import frontsnapk1ck.alloy.main.Alloy;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    private List<TrackListener> tListeners;

    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player) 
    {
        this.player = player;
        this.queue = new LinkedBlockingQueue<AudioTrack>();
        this.tListeners = new ArrayList<TrackListener>();
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public int queue(AudioTrack track) 
    {
        int index = queue.size();
        queue(track , index);
        return index;
    }

    public void queueTop(AudioTrack track)
    {
        queue(track , 0);
    }

	public void queue(AudioTrack track , int index) 
    {
        if (!player.startTrack(track, true)) 
        {
            List<AudioTrack> tracks = new ArrayList<AudioTrack>();
            queue.drainTo(tracks);
            tracks.add(index, track);
            for (AudioTrack t : tracks) 
                queue.offer(t);
        }
	}

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() 
    {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) 
    {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) 
        {
            try 
            {
                nextTrack();
            }
            catch (Exception e) 
            {
                Alloy.LOGGER.warn("TrackScheduler", "Cannot play the same instance of a track twice");
            }
        }
        for (TrackListener l : tListeners)
            l.onTrackEnd();
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) 
    {
        for (TrackListener l : tListeners)
            l.onTrackStart();
    }

    /**
     * 
     * @return the current queue
     */
    public BlockingQueue<AudioTrack> getQueue() 
    {
        return queue;
    }

    /**
     * clears the queue
     */
    public void clear() 
    {
        this.queue.clear();
    }

    public AudioTrack removeSong(int index) 
    {
        List<AudioTrack> tracks = new ArrayList<AudioTrack>();
        queue.drainTo(tracks);
        AudioTrack out = tracks.remove(index);
        for (AudioTrack t : tracks) 
            queue.offer(t);

        return out;
    }

    public void addListener(TrackListener l) 
    {
        this.tListeners.add(l);
    }

    public boolean removeListener(TrackListener l) 
    {
        return this.tListeners.remove(l);
    }
}