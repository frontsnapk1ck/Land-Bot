package alloy.command.voice;

import alloy.command.util.AbstractCommand;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class LeaveCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        try {
            VoiceChannel vc = g.getAudioManager().getConnectedChannel();
            g.getAudioManager().closeAudioConnection();
            Template t = Templates.voiceDisconnectSuccess(vc);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("LeaveCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);

        } catch (Exception e) {
            Template t = Templates.voiceDisconnectFail();
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("LeaveCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }
    }

}