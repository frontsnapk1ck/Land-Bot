package alloy.command.voice;

import alloy.audio.GuildMusicManager;
import alloy.command.util.AbstractCommand;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Audible;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.templates.Templates;
import alloy.utility.discord.perm.AlloyPerm;
import alloy.utility.discord.perm.DisPerm;
import disterface.util.template.Template;
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
        //TODO make it so that you need a majority to skip
        TextChannel channel = data.getChannel();
        Guild g = data.getGuild();
        Audible audible = data.getAudible();
        Sendable bot = data.getSendable();

        GuildMusicManager musicManager = audible.getGuildAudioPlayer(g);
        musicManager.scheduler.nextTrack();
    
        Template t = Templates.musicSkipped();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }
    
}
