package botcord.components.gui.base;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import botcord.event.BotCordListener;

@SuppressWarnings("serial")
public abstract class BaseBCButton extends JButton  {

    private List<BotCordListener> listeners;

    public BaseBCButton() 
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
