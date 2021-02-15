package botcord.manager;

import java.util.ArrayList;
import java.util.List;

import botcord.event.BCListener;
import botcord.event.PressEvent;
import botcord.event.SwitchTarget;
import botcord.manager.util.ScreenProxy;
import botcord.manager.util.SwitchEvent;
import botcord.manager.util.Switchable;
import botcord.screen.GuildScreen;
import botcord.screen.PMScreen;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class ScreenSwitchManager implements BCListener {

    private List<Switchable> listeners;
    private ScreenProxy proxy;

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
        if (e.getTarget().equals(SwitchTarget.GUILD))
            guild((Guild)e.getData());
        if (e.getTarget().equals(SwitchTarget.DEBUG))
            debug();
        if (e.getTarget().equals(SwitchTarget.PM))
            pm();
        if (e.getTarget().equals(SwitchTarget.CHANNEL))
            channel((GuildChannel)e.getData());
        if (e.getTarget().equals(SwitchTarget.PRIVATE_CHANNEL))
            channel((PrivateChannel)e.getData());

    }

    private void channel(PrivateChannel data) 
    {
        PMScreen screen = (PMScreen) this.proxy.getCurrentScreen();
        screen.setActiveChannel(data);
    }

    private void channel(GuildChannel data) 
    {
        GuildScreen screen = (GuildScreen)(this.proxy.getCurrentScreen());
        screen.setActiveChannel(data);
    }

    private void debug() 
    {
        SwitchEvent se = new SwitchEvent(this.proxy.getDebug());
        for (Switchable switchable : listeners) 
            switchable.onSwitch(se);
    }

    private void pm() 
    {
        SwitchEvent se = new SwitchEvent(this.proxy.getPm());
        for (Switchable switchable : listeners) 
            switchable.onSwitch(se);
	}

    private void guild(Guild guild) 
    {
        SwitchEvent se = new SwitchEvent(new GuildScreen(guild));
        for (Switchable switchable : listeners) 
            switchable.onSwitch(se);
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

    public void setProxy(ScreenProxy proxy) 
    {
        this.proxy = proxy;
    }
    
}
