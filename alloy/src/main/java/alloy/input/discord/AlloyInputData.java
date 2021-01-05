package alloy.input.discord;

import alloy.gameobjects.Server;
import alloy.main.Moderator;
import alloy.main.Queueable;
import alloy.main.Sendable;
import alloy.main.handler.CooldownHandler;
import alloy.utility.discord.perm.DisPerm;
import input.device.InputType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class AlloyInputData {
    
    private AlloyInputEvent event;
    private Server server;
    private InputType inputType;
    private String description;
    private boolean cooldown;
    private DisPerm requiredPerm;
    private Sendable sendable;
    private Moderator moderator;
    private CooldownHandler cooldownHandler;
    private Queueable queue;

    public AlloyInputData() 
    {
        this.event = new AlloyInputEvent();
    }

    public Sendable getSendable() 
    {
        return sendable;
    }

    public String getDescription() 
    {
        return description;
    }

    public AlloyInputEvent getEvent() 
    {
        return event;
    }

    public InputType getInputType() 
    {
        return inputType;
    }

    public DisPerm getRequiredPerm() 
    {
        return requiredPerm;
    }

    public Server getServer() 
    {
        return server;
    }

    public TextChannel getChannel() 
    {
		return this.event.getChannel();
	}

    public GuildMessageReceivedEvent getMessageEvent() 
    {
		return this.event.getEvent();
	}

    public Guild getGuild() 
    {
		return this.event.getGuild();
	}

    public String getMessage() 
    {
		return this.event.getMessage();
	}

    public User getUser() 
    {
		return this.event.getUser();
    }
    
    public Moderator getModerator()
    {
        return moderator;
    }

    public Queueable getQueue()
    {
        return queue;
    }

    public Message getMessageActual() 
    {
		return this.event.getMessageActual();
	}

    public boolean isCooldown() 
    {
        return cooldown;
    }

    public JDA getJDA() 
    {
		return this.event.getJda();
    }
    
    public CooldownHandler getCooldownHandler() 
    {
        return cooldownHandler;
    }

    public void setBot(Sendable sendable) 
    {
        this.sendable = sendable;
    }

    public void setCooldown(boolean cooldown) 
    {
        this.cooldown = cooldown;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public void setEvent(AlloyInputEvent event) 
    {
        this.event = event;
    }

    public void setInputType(InputType inputType) 
    {
        this.inputType = inputType;
    }

    public void setRequiredPerm(DisPerm requiredPerm) 
    {
        this.requiredPerm = requiredPerm;
    }

    public void setServer(Server server) 
    {
        this.server = server;
    }

    public void setMod(Moderator bot) 
    {
        this.moderator = bot;
	}

    public void setAuthor(User author) 
    {
        this.event.setAuthor(author);
	}

    public void setGuild(Guild guild) 
    {
        this.event.setGuild(guild);
	}

    public void setChannel(TextChannel channel) 
    {
        this.event.setChannel(channel);
	}

    public void setJDA ( JDA jda )
    {
        this.event.setJda(jda);
    }

    public void setCooldown(CooldownHandler bot) 
    {
        this.cooldownHandler = bot;
	}

    public void setTrigger(String trigger) 
    {
        this.event.setTrigger(trigger);
	}

    public void setQueue(Queueable queue)
    {
        this.queue = queue;
    }

}
