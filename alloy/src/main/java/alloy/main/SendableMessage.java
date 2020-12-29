package alloy.main;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class SendableMessage {
    

    private MessageChannel channel;
    private MessageEmbed messageE;
    private String messageS;
    private String from;

    public SendableMessage() 
    {
        this.from = "NO CHANNEL PROVIDED";
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public String getFrom() {
        return from;
    }

    public MessageEmbed getMessageE() {
        return messageE;
    }

    public String getMessageS() {
        return messageS;
    }

    public void setChannel(MessageChannel channel) {
        this.channel = channel;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setMessage(MessageEmbed messageE) 
    {
        this.messageE = messageE;
    }

    public void setMessage(String messageS) 
    {
        this.messageS = messageS;
    }

    public boolean hasChannel ()
    {
        return this.channel != null;
    }
    
    public boolean hasMessageE ()
    {
        return this.messageE != null;
    }
    
    public boolean hasMessageS ()
    {
        return this.messageS != null;
    }
    
    public boolean hasFrom ()
    {
        return this.from != null;
    }
    

}
