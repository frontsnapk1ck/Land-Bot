package frontsnapk1ck.alloy.input.discord;

import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.input.Input;
import frontsnapk1ck.input.device.InputType;
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

    public AlloyInput(String name, String command, DisPerm p, boolean b, String description, InputType type) 
    {
        super(name, command);
        this.data = new AlloyInputData();
        this.data.setRequiredPerm( p );
        this.data.setCooldown( b );
        this.data.setDescription( description );
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
        this.data.setBotHandler( bot );
        this.data.setMod( bot );
        this.data.setCooldown( bot );
        this.data.setQueue( bot );
        this.data.setAudible( bot );
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
