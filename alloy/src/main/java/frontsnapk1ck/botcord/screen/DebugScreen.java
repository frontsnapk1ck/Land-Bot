package frontsnapk1ck.botcord.screen;

import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.botcord.components.selector.ScreenSelector;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.screen.util.BotCordScreen;
import net.dv8tion.jda.api.JDA;

public class DebugScreen extends BotCordScreen {

    private JDA jda;
    private List<BCListener> listeners;

    public DebugScreen(JDA jda) 
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
        this.listeners = new ArrayList<BCListener>();
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

    public void setListeners(List<BCListener> listeners) 
    {
        this.listeners = listeners;
        getSelector().updateListeners(this.listeners);
    }

    public List<BCListener> getListeners() 
    {
        return listeners;
    }

    public void addListener(BCListener l)
    {
        this.listeners.add(l);
        getSelector().updateListeners(this.listeners);
    }
    
    public boolean rmListener(BCListener l)
    {
        boolean b = this.listeners.remove(l);
        getSelector().updateListeners(this.listeners);
        return b;
    }
    
}
