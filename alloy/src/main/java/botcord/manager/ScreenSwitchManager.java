package botcord.manager;

import java.util.ArrayList;
import java.util.List;

import botcord.event.BotCordListener;
import botcord.event.PressEvent;
import botcord.event.PressTarget;
import botcord.manager.util.SwitchEvent;
import botcord.manager.util.Switchable;
import net.dv8tion.jda.api.entities.Guild;

public class ScreenSwitchManager implements BotCordListener {

    private List<Switchable> listeners;

    public ScreenSwitchManager() 
    {
        super();
        init();
    }

    private void init() 
    {
        this.listeners = new ArrayList<Switchable>();
    }

    @Override
    public void onPress(PressEvent e) 
    {
        if (e.getTarget().equals(PressTarget.GUILD))
            guild((Guild)e.getData());
        if (e.getTarget().equals(PressTarget.DEBUG))
            debug();
        if (e.getTarget().equals(PressTarget.PM))
            pm();
        
        SwitchEvent se = new SwitchEvent();
        for (Switchable switchable : listeners) 
            switchable.onSwitch(se);
    }

    private void debug() 
    {
        System.err.println("DEBUG");
    }

    private void pm() 
    {
        System.err.println("PM");
	}

    private void guild(Guild guild) 
    {
        System.err.println(guild.getName());
    }

    public void setListeners(List<Switchable> listeners) 
    {
        this.listeners = listeners;
    }

    public List<Switchable> getListeners() 
    {
        return listeners;
    }

    public void addListener(Switchable l)
    {
        this.listeners.add(l);
    }
    
    public boolean rmListener(Switchable l)
    {
        return this.listeners.remove(l);
    }
    
}
