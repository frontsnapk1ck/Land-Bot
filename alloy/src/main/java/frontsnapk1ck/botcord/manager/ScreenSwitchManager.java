package frontsnapk1ck.botcord.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import frontsnapk1ck.alloy.main.intefs.Queueable;
import frontsnapk1ck.alloy.utility.job.jobs.AlloyDelayJob;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.event.PressEvent;
import frontsnapk1ck.botcord.event.SwitchTarget;
import frontsnapk1ck.botcord.manager.util.ScreenProxy;
import frontsnapk1ck.botcord.manager.util.SwitchEvent;
import frontsnapk1ck.botcord.manager.util.Switchable;
import frontsnapk1ck.botcord.screen.GuildScreen;
import frontsnapk1ck.botcord.screen.PMScreen;
import frontsnapk1ck.utility.event.Job;
import frontsnapk1ck.utility.event.annotation.RequiredJob;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;

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
        AlloyDelayJob<PressEvent> j = new AlloyDelayJob<PressEvent>(c, e);
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
