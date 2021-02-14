package alloy.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private List<AttachedFile> files;

    public SendableMessage() 
    {
        this.from = "NO CHANNEL PROVIDED";
        this.files = new ArrayList<AttachedFile>();
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

    public List<AttachedFile> getFiles() {
        return files;
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

    public void addFile(File file) 
    {
        this.addFile(file,false);
	}

    public void addFile(File file, boolean save) 
    {
        this.files.add(new AttachedFile(file, save));
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

    public boolean hadFiles()
    {
        return this.files.size() != 0;
    }
    
    public class AttachedFile {
        
        public boolean save;
        public File file;

        public AttachedFile(File f, boolean b)
        {
            this.file = f;
            this.save = b;
        }

    }

}
