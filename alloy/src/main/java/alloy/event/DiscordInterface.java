package alloy.event;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import alloy.event.util.AlloyDIClient;
import alloy.event.util.AlloyDIUtil;
import disterface.util.template.Template;
import alloy.templates.Templates;
import disterface.DisInterClient;
import disterface.MessageData;
import disterface.MessageData.Destination;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import utility.logger.Level;
import utility.logger.Logger;

public class DiscordInterface implements DebugListener {

    private Map<Level, TextChannel> logs;
    private Logger logger;
    private DisInterClient disterface;

    public DiscordInterface(Map<Level, TextChannel> logs) 
    {
        super();
        this.logs = logs;
        this.logger = new Logger();
        try 
        {
            this.disterface = new AlloyDIClient();
        }
        catch (IOException e) 
        {
            logger.error("DiscordInterface", e);
            e.printStackTrace();
        }
    }

	@Override
    public void onReceive(DebugEvent e) 
    {
        Destination destination = AlloyDIUtil.parse(e.getLevel());
        Template t = Templates.debug(e);
        MessageData data = new MessageData(t,destination);
        try 
        {
            this.disterface.send(data);
        }
        catch (Exception ex) 
        {
            logger.error("DiscordInterface", ex);
        }
        return;
	}

    public boolean debugFile(Message m, File down) 
    {
        TextChannel log = this.logs.get(Level.DEBUG);

        try 
        {
            log.sendMessage(m).addFile(down).complete();
            return true;
        }
        catch (IllegalArgumentException ex) 
        {
            return false;
        }
        catch (Exception ex)
        {
            logger.error("DiscordInterface", ex);
            return false;
        }
	}

	public void resetSocket() 
    {
        try 
        {
            this.disterface = new AlloyDIClient();
        }
        catch (IOException e) 
        {
            logger.error("DiscordInterface", e);
            e.printStackTrace();
        }
	}
    
}
