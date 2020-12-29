package alloy.handler;

import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class VoiceHandeler {

    public static boolean join(VoiceChannel channel) 
    {
        AudioManager manager = channel.getGuild().getAudioManager();
        try 
        {
            manager.openAudioConnection(channel);
            return true;
        }
        catch (Exception e) 
        {
            return false;
        }
    }

    public static boolean memberIn(VoiceChannel vc, Member m) 
    {
        List<Member> members = vc.getMembers();
        for (Member member : members) 
        {
            if (member.getIdLong() == m.getIdLong() )
                return true;
        }
        return false;
	}

}
