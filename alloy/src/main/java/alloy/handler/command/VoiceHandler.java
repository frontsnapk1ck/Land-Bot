package alloy.handler.command;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class VoiceHandler {

    public static final int        NUMBER_OF_VIDEOS_RETURNED = 25;

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    // private static YouTube youtube;

    public static boolean join(VoiceChannel channel) {
        AudioManager manager = channel.getGuild().getAudioManager();
        try {
            manager.openAudioConnection(channel);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean memberIn(VoiceChannel vc, Member m) {
        List<Member> members = vc.getMembers();
        for (Member member : members) 
        {
            if (member.getIdLong() == m.getIdLong())
                return true;
        }
        return false;
    }

    public static boolean isConnected(Guild g) 
    {
        AudioManager manager = g.getAudioManager();
        return manager.isConnected();
    }

    public static String getUrl(String[] args) 
    {
        if (isLink(args))
            return args[0];
        else
            return searchYouTube(args);
    }

    private static boolean isLink(String[] args) 
    {
        URL url;
        try {
            url = new URL(args[0]);
            return  url.getProtocol().equalsIgnoreCase("http") || 
                    url.getProtocol().equalsIgnoreCase("https");
        } catch (MalformedURLException e) 
        {
            return false;
        }
    }

    private static String searchYouTube(String[] args) 
    {
        return args[0];
    }

}
