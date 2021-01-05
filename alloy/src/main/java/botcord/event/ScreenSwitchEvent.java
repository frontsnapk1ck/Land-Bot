package botcord.event;

import java.awt.event.ActionEvent;

import net.dv8tion.jda.api.entities.Guild;

@SuppressWarnings("serial")
public class ScreenSwitchEvent extends ActionEvent {

    private Guild guild;

    public ScreenSwitchEvent(Object source, int id, String command, long when, int modifiers) 
    {
        super(source, id, command, when, modifiers);
    }

    public ScreenSwitchEvent(Object source, int id, String command, int modifiers) 
    {
        super(source, id, command, modifiers);
    }

    public ScreenSwitchEvent(Object source, int id, String command)
    {
        super(source, id, command);
    }
    
    public ScreenSwitchEvent(ActionEvent e)
    {
        this(e.getSource(), e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers());
    }

    public Guild getGuild() 
    {
        return this.guild;
    }
    
    public ScreenSwitchEvent setGuild(Guild guild) 
    {
        this.guild = guild;
        return this;
    }
    
}
