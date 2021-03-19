package frontsnapk1ck.alloy.event;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import frontsnapk1ck.alloy.event.util.AlloyDIClient;
import frontsnapk1ck.alloy.event.util.AlloyDIUtil;
import frontsnapk1ck.disterface.util.template.Template;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.disterface.DisInterClient;
import frontsnapk1ck.disterface.MessageData;
import frontsnapk1ck.disterface.MessageData.Destination;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import frontsnapk1ck.utility.logger.Level;
import frontsnapk1ck.utility.logger.Logger;

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

    public void clientSend(Template t , Destination destination) 
    {
        this.clientSend(new MessageData(Arrays.asList(t), destination));
    }
    
    public void clientSend(MessageData data) 
    {
        try {
            this.disterface.send(data);
        } catch (IOException e) 
        {
            logger.error("DiscordInterface", e);
            e.printStackTrace();
        }
    }
    
}
