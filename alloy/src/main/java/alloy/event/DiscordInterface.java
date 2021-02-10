package alloy.event;

import java.util.Map;

import alloy.templates.Template;
import alloy.templates.Templates;
import net.dv8tion.jda.api.entities.TextChannel;
import utility.logger.Level;
import utility.logger.Logger;

public class DiscordInterface implements DebugListener {

    private Map<Level, TextChannel> logs;
    private Logger logger;

    public DiscordInterface(Map<Level , TextChannel> logs)
    {
        super();
        this.logs = logs;
        this.logger = new Logger();
    }

	@Override
    public void onReceive(DebugEvent e) 
    {
        TextChannel log = this.logs.get(e.getLevel());
        if (log == null)
            return;
        Template t = Templates.debug(e);
        try 
        {
            log.sendMessage(t.getEmbed()).complete();
        }
        catch (Exception ex) 
        {
            logger.error("DiscordInterface", ex);
        }
        return;
	}
    
}
