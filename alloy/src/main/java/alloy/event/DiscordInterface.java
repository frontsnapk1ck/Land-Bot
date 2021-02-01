package alloy.event;

import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import net.dv8tion.jda.api.entities.TextChannel;

public class DiscordInterface implements DebugListener {

    private TextChannel log;
    private Sendable bot;

    public DiscordInterface(TextChannel log, Sendable bot)
    {
        super();
        this.log = log;
        this.bot = bot;
    }

	@Override
    public void onReceive(DebugEvent e) 
    {
        if (this.log == null)
            return;
        Template t = Templates.debug(e);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(log);
        sm.setFrom("DiscordInterface");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        return;
	}
    
}
