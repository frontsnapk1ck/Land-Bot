package alloy.main;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class SendableMessage {
    

    private MessageChannel channel;
    private MessageEmbed messageE;
    private String messageS;
    private String from;
    private Message message;

    private Message sent;

    public SendableMessage() 
    {
        this.from = "NO CHANNEL PROVIDED";
    }

    public MessageChannel getChannel()
    {
        return channel;
    }

    public String getFrom()
    {
        return from;
    }

    public MessageEmbed getMessageE()
    {
        return messageE;
    }

    public String getMessageS()
    {
        return messageS;
    }

    public Message getMessage() 
    {
        return message;
    }

    public Message getSent() {
        return sent;
    }

    public void setChannel(MessageChannel channel)
    {
        this.channel = channel;
    }

    public void setFrom(String from)
    {
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

    public void setMessage(Message message) 
    {
        this.message = message;
    }
    
    public void setSent(Message sent) 
    {
        this.sent = sent;
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

    public boolean hasMessage ()
    {
        return this.message != null;
    }
    
    public boolean hasFrom ()
    {
        return this.from != null;
    }

    public boolean hasSent()
    {
        return this.sent != null;
    }
    

}
