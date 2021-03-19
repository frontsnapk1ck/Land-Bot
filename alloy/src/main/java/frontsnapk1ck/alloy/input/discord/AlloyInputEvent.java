package frontsnapk1ck.alloy.input.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class AlloyInputEvent {

    private GuildMessageReceivedEvent event;
    private String message;
    private Message messageActual;
    private User user;
    private TextChannel channel;
    private Guild guild;
    private JDA jda;

    public AlloyInputEvent(Guild g, TextChannel c, User u, Message m, GuildMessageReceivedEvent e) 
    {
        this.guild = g;
        this.jda = g.getJDA();
        this.channel = c;
        this.user = u;
        this.message = m.getContentRaw();
        this.messageActual = m;
        this.event = e;
    }
    
    public AlloyInputEvent()
    {
	}

	public TextChannel getChannel()
    {
        return channel;
    }

    public GuildMessageReceivedEvent getEvent()
    {
        return event;
    }

    public Guild getGuild()
    {
        return guild;
    }

    public String getMessage() 
    {
        return message;
    }

    public User getUser()
    {
        return user;
    }

    public JDA getJda()
    {
        return jda;
    }

    public void setGuild(Guild guild) 
    {
        this.guild = guild;
	}

    public void setAuthor(User author) 
    {
        this.user = author;
    }
    
    public void setChannel(TextChannel channel)
    {
        this.channel = channel;
    }

    public void setEvent(GuildMessageReceivedEvent event)
    {
        this.event = event;
    }

    public void setMessageActual(Message message)
    {
        this.messageActual = message;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public void setJda(JDA jda)
    {
        this.jda = jda;
    }

    public void setTrigger(String trigger) 
    {
        this.message = trigger;
	}

    public Message getMessageActual() 
    {
        return this.messageActual;
	}
    
}
