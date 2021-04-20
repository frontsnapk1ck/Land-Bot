package frontsnapk1ck.alloy.handler.util;

import java.io.OutputStream;
import java.io.PrintStream;

import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

public class DisPrintProxy extends PrintStream {

    public final long GUILD_ID      = 833530318790459412L;
    public final long CHANNEL_ID    = 833530319407546373L;

    private TextChannel channel;
    private Sendable sendable;

    public DisPrintProxy(OutputStream out, JDA jda , Sendable sendable) 
    {
        super(out);
        this.channel = jda.getGuildById(GUILD_ID).getTextChannelById(CHANNEL_ID);
        this.sendable = sendable;
    }

    @Override
    public void print(Object obj) 
    {
        SendableMessage sm = new SendableMessage();
        sm.setMessage(String.valueOf(obj));
        sm.setFrom(getClass());
        sm.setChannel(channel);
        sendable.send(sm);
    }

    @Override
    public void print(String s) 
    {
        this.print((Object)s);
    }

}
