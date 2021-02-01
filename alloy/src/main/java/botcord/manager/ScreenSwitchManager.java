package botcord.manager;

import java.util.ArrayList;
import java.util.List;

import botcord.BotCord;
import botcord.event.BotCordListener;
import botcord.event.PressEvent;
import botcord.event.SwitchTarget;
import botcord.manager.util.ScreenProxy;
import botcord.manager.util.SwitchEvent;
import botcord.manager.util.Switchable;
import botcord.screen.GuildScreen;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;

public class ScreenSwitchManager implements BotCordListener {

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
    }

    private void channel(GuildChannel data) 
    {
        GuildScreen screen = (GuildScreen)(this.proxy.getCurrentScreen());
        screen.setActiveChannel(data);
    }

    private void debug() 
    {
        BotCord.LOGGER.info("ScreenSwitchManager" , "Switched to Debug Screen");
        SwitchEvent se = new SwitchEvent(this.proxy.getDebug());
        for (Switchable switchable : listeners) 
            switchable.onSwitch(se);
    }

    private void pm() 
    {
        BotCord.LOGGER.info("ScreenSwitchManager" , "Switched to PM Screen");
        
        SwitchEvent se = new SwitchEvent(this.proxy.getPm());
        for (Switchable switchable : listeners) 
            switchable.onSwitch(se);
	}

    private void guild(Guild guild) 
    {
        BotCord.LOGGER.info("ScreenSwitchManager" , "Switched to Guild Screen for the guild " + guild.getName());
        
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
