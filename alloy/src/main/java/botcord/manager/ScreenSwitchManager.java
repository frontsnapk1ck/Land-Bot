package botcord.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import alloy.main.intefs.Queueable;
import alloy.utility.job.jobs.DelayJob;
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
import utility.event.Job;
import utility.event.annotation.RequiredJob;

public class ScreenSwitchManager implements BCListener {

    private List<Switchable> listeners;
    private ScreenProxy proxy;
    private Queueable queueable;
    private List<Job> tmpQueue;

    public ScreenSwitchManager()
    {
        super();
        init();
    }

    private void init() 
    {
        this.listeners = new ArrayList<Switchable>();
        this.tmpQueue = new ArrayList<Job>();
    }

    @Override
    @RequiredJob
    public void onPress(PressEvent e) 
    {
        Consumer<PressEvent> c = new Consumer<PressEvent>()
        {
            @Override
            public void accept(PressEvent e) 
            {
                onPressImp(e);
            }
        };
        DelayJob<PressEvent> j = new DelayJob<PressEvent>(c, e);
        if (this.queueable == null)
            this.tmpQueue.add(j);
        else
            this.queueable.queue(j);
    }

    protected void onPressImp(PressEvent e) 
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

    public void setQueueable(Queueable queueable) 
    {
        this.queueable = queueable;
        for (Job job : this.tmpQueue) 
            this.queueable.queue(job);
    }
    
}
