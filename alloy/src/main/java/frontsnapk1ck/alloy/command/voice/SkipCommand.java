package frontsnapk1ck.alloy.command.voice;

import frontsnapk1ck.alloy.audio.GuildMusicManager;
import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.handler.command.AudioHandler;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Audible;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class SkipCommand extends AbstractCommand {

    public static final float MAJORITY = 0.6f;

    @Override
    public void execute(AlloyInputData data)
    {
        TextChannel channel = data.getChannel();
        Guild g = data.getGuild();
        Audible audible = data.getAudible();
        Sendable bot = data.getSendable();
        User author = data.getUser();

        if(!AudioHandler.isConnected(g))
        {
            Template t = Templates.notInVoiceChannel();
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        GuildMusicManager musicManager = audible.getGuildAudioPlayer(g);
        int numInVC = getNumInVC(g);
        int majority = Math.round(numInVC * MAJORITY);

        if (musicManager.getPlayer().getPlayingTrack() == null)
        {
            Template t = Templates.noMusicPlaying();
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (alreadyVoted(musicManager , data))
        {
            Template t = Templates.alreadyVotedSkip();
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        musicManager.addSkipped(author);
        int voteNum = musicManager.getSkipped().size();
        Template t = Templates.voteSkip( voteNum , majority );
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);

        if (enoughVotes(musicManager , majority))
        {
            Template t2 = Templates.musicSkipped();
            SendableMessage sm2 = new SendableMessage();
            sm2.setChannel(channel);
            sm2.setFrom(getClass());
            sm2.setMessage(t2.getEmbed());
            bot.send(sm2);
            musicManager.nextTrack();
        }
    }

    private int getNumInVC(Guild g) 
    {
        return g.getAudioManager()
                .getConnectedChannel()
                .getMembers()
                .size();
    }

    private boolean alreadyVoted(GuildMusicManager musicManager, AlloyInputData data) 
    {
        User u = data.getUser();
        return musicManager.getSkipped().contains(u);
    }

    private boolean enoughVotes(GuildMusicManager musicManager, int majority) 
    {
        return musicManager.getSkipped().size() >= majority;
    }
    
}
