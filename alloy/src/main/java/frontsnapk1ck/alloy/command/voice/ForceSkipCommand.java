package frontsnapk1ck.alloy.command.voice;

import frontsnapk1ck.alloy.audio.GuildMusicManager;
import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.handler.command.AudioHandler;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Audible;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.perm.AlloyPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class ForceSkipCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() 
    {
        return AlloyPerm.MANAGE_SERVER;
    }

    @Override
    public void execute(AlloyInputData data)
    {
        TextChannel channel = data.getChannel();
        Guild g = data.getGuild();
        Audible audible = data.getAudible();
        Sendable bot = data.getSendable();
        User author = data.getUser();
        Member m = g.getMember(author);

        if (!DisPermUtil.checkPermission(m, getPermission()))
        {
            AlloyTemplate t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);   
            return;
        }

        if(!AudioHandler.isConnected(g))
        {
            AlloyTemplate t = Templates.notInVoiceChannel();
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        GuildMusicManager musicManager = audible.getGuildAudioPlayer(g);
        
        if (musicManager.getPlayer().getPlayingTrack() == null)
        {
            AlloyTemplate t = Templates.noMusicPlaying();
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
        
        musicManager.nextTrack();
        AlloyTemplate t = Templates.musicForceSkipped();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }
    
}