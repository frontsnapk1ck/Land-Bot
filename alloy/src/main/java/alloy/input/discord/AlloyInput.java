package alloy.input.discord;

import alloy.gameobjects.Server;
import alloy.main.Alloy;
import alloy.utility.discord.perm.DisPerm;
import input.Input;
import input.device.InputType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class AlloyInput extends Input {

    private AlloyInputData data;

    public AlloyInput(String name, AlloyInputEvent event)
    {
        super(name , event.getMessage());       
        this.data = new AlloyInputData();
        this.data.setEvent(event);
    }

    public AlloyInput(String name, String command, DisPerm p, boolean b, String descrption, InputType type) 
    {
        super(name, command);
        this.data = new AlloyInputData();
        this.data.setRequiredPerm( p );
        this.data.setCooldown( b );
        this.data.setDescription( descrption );
        this.data.setInputType( type );
	}

	public TextChannel getChannel() 
    {
        return data.getChannel();
    }

    public GuildMessageReceivedEvent getMessageEvent() 
    {
        return data.getMessageEvent();
    }

    public Guild getGuild() 
    {
        return data.getGuild();
    }

    public String getMessage() 
    {
        return data.getMessage();
    }

    public User getUser() 
    {
        return data.getUser();
    }

    public AlloyInputEvent getEvent() 
    {
        return data.getEvent();
    }

    public Server getServer() 
    {
        return data.getServer();
    }

    public String getDescription() 
    {
        return data.getDescription();
    }

    public InputType getInputType() 
    {
        return data.getInputType();
    }

    public DisPerm getRequiredPerm() 
    {
        return data.getRequiredPerm();
    }

    public boolean isCooldown() 
    {
        return data.isCooldown();
    }
    
    public AlloyInputData getData() 
    {
        return data;
    }

    public JDA getJDA()
    {
        return data.getJDA();
    }

    public void setServer(Server s) 
    {
        this.data.setServer( s );
	}

    public void setBot(Alloy bot) 
    {
        this.data.setBot( bot );
        this.data.setMod( bot );
        this.data.setCooldown( bot );
        this.data.setQueue( bot );
    }

    public void setJDA ( JDA jda )
    {
        this.data.setJDA(jda);
    }

    public void setTrigger(String trigger) 
    {
        this.data.setTrigger(trigger);
        super.setTrigger(trigger);
	}
    
}
