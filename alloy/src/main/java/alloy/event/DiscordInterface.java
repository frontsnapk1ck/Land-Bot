package alloy.event;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import alloy.templates.Template;
import alloy.templates.Templates;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import utility.Util;
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
        List<MessageEmbed> embeds = getDebugEmbeds(e);
        try 
        {
            for (MessageEmbed messageEmbed : embeds) 
                log.sendMessage(messageEmbed).complete();
        }
        catch (Exception ex) 
        {
            logger.error("DiscordInterface", ex);
        }
        return;
	}

	private List<MessageEmbed> getDebugEmbeds(DebugEvent e) 
    {
        List<MessageEmbed> embeds = new ArrayList<MessageEmbed>();
        Template t = Templates.debug(e);
        try {
            embeds.add(t.getEmbed());
        } catch (Exception ex) 
        {
            String[] arr = t.getText().split("\n");
            String title = arr[0];
            String[] newLines = Util.arrRange(arr, 1);

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(title);

            String out = "";
            for (int i = 0; i < newLines.length; i++) 
            {
                String tmp = out + newLines[i] + "\n";
                if (tmp.length() > MessageEmbed.TEXT_MAX_LENGTH)
                {
                    eb.setDescription(out);
                    embeds.add(eb.build());
                    out = newLines[i];
                }
                else
                    out = tmp;
            }
            eb.setDescription(out);
            embeds.add(eb.build());
        }
        return embeds;
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
    
}
