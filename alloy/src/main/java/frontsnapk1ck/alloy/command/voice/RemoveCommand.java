package frontsnapk1ck.alloy.command.voice;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import frontsnapk1ck.alloy.audio.GuildMusicManager;
import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Audible;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.utility.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class RemoveCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Audible audible = data.getAudible();


        if (args.length < 1) 
        {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
        
        if (!Util.validInt(args[0])) 
        {
            AlloyTemplate t = Templates.invalidNumberFormat(args[0]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        int index = Integer.parseInt(args[0]);
        GuildMusicManager musicManager = audible.getGuildAudioPlayer(g);

        if (index < 0 || index > musicManager.getScheduler().getQueue().size())
        {
            AlloyTemplate t = Templates.numberOutOfBounds(new IndexOutOfBoundsException(index - 1));
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        AudioTrack removed = musicManager.getScheduler().removeSong(index - 1);

        AlloyTemplate t = Templates.songRemove(removed);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }
    
}
