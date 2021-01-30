package botcord.screen;

import java.util.ArrayList;
import java.util.List;

import botcord.components.selector.ScreenSelector;
import botcord.event.BotCordListener;
import botcord.screen.util.BotCordScreen;
import net.dv8tion.jda.api.JDA;

public class PMScreen extends BotCordScreen {

    private JDA jda;
    private List<BotCordListener> listeners;

    public PMScreen(JDA jda) 
    {
        this.jda = jda;
        init();
        config();
    }

    @Override
    protected void onScreenResize() 
    {
        update();
    }

    @Override
    public void init() 
    {
        this.setSelector(new ScreenSelector(this.jda));
        this.listeners = new ArrayList<BotCordListener>();
    }

    @Override
    public void config() 
    {
        this.configSelector();
    }

    @Override
    public void configSelector() 
    {
        updateBounds();
        this.getPanel().add(this.getSelector());
    }

    private void updateBounds() 
    {
        int x = 0;
        int y = 0;
        int width  = (int)( this.getWidth()  * SELECTOR_WIDTH);
        int height = (int)( this.getHeight() * 1f);

        this.getSelector().setBounds(x, y, width, height);
    }

    @Override
    public void update() 
    {
        updateBounds();
        this.getSelector().update();
    }

    public void setListeners(List<BotCordListener> listeners) 
    {
        this.listeners = listeners;
        getSelector().updateListeners(this.listeners);
    }

    public List<BotCordListener> getListeners() 
    {
        return listeners;
    }

    public void addListener(BotCordListener l)
    {
        this.listeners.add(l);
        getSelector().updateListeners(this.listeners);
    }
    
    public boolean rmListener(BotCordListener l)
    {
        boolean b = this.listeners.remove(l);
        getSelector().updateListeners(this.listeners);
        return b;
    }
    
}
