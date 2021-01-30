package botcord.screen;

import java.util.ArrayList;
import java.util.List;

import botcord.components.selector.ChannelSelector;
import botcord.components.selector.ScreenSelector;
import botcord.event.BotCordListener;
import botcord.screen.util.BotCordScreen;
import net.dv8tion.jda.api.entities.Guild;

public class GuildScreen extends BotCordScreen {

    private Guild guild;
    private List<BotCordListener> listeners;
    private ChannelSelector channelSelector;

    public GuildScreen(Guild guild) 
    {
        super();
        this.guild = guild;
        this.init();
        this.config();
    }

    @Override
    protected void onScreenResize() 
    {
        update();    
    }

    @Override
    public void init() 
    {
        this.setSelector(new ScreenSelector(this.guild.getJDA()));
        this.channelSelector = new ChannelSelector(this.guild);
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
        super.configSelector();
        this.getPanel().add(this.channelSelector);

    }

    private void updateBounds() 
    {
        updateSelectorBounds();
        updateChannelBounds();
    }

    private void updateSelectorBounds() 
    {
        int x = 0;
        int y = 0;
        int width  = (int)( this.getWidth()  * SELECTOR_WIDTH);
        int height = (int)( this.getHeight() * 1f);

        this.getSelector().setBounds(x, y, width, height);
    }

    private void updateChannelBounds() {
    }

    @Override
    public void update() 
    {
        updateBounds();
        this.getSelector().update();
        this.channelSelector.update();
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
