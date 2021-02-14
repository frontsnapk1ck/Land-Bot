package botcord.components.gui;

import java.util.ArrayList;
import java.util.List;

import botcord.components.gui.base.BaseBCButton;
import botcord.event.BotCordListener;
import botcord.util.BCComponent;

@SuppressWarnings("serial")
public abstract class BCButton extends BaseBCButton implements BCComponent {

    private List<BotCordListener> listeners;

    public BCButton() 
    {
        super();
        this.listeners = new ArrayList<BotCordListener>();
    }

    protected abstract void configListener();

    public void setListeners(List<BotCordListener> listeners) 
    {
        this.listeners = listeners;
    }

    public List<BotCordListener> getListeners() {
        return listeners;
    }

    public void addListener(BotCordListener l)
    {
        this.listeners.add(l);
    }
    
    public boolean rmListener(BotCordListener l)
    {
        return this.listeners.remove(l);
    }
}
