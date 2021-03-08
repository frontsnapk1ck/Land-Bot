package alloy.command.voice;

import alloy.audio.GuildMusicManager;
import alloy.command.util.AbstractCommand;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Audible;
import alloy.utility.discord.perm.AlloyPerm;
import alloy.utility.discord.perm.DisPerm;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class SkipCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() 
    {
        return AlloyPerm.CREATOR;
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        TextChannel channel = data.getChannel();
        Guild g = data.getGuild();
        Audible audible = data.getAudible();

        GuildMusicManager musicManager = audible.getGuildAudioPlayer(g);
        musicManager.scheduler.nextTrack();
    
        channel.sendMessage("Skipped to next track.").queue();
    }
    
}
