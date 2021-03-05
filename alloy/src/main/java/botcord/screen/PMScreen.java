package botcord.screen;

import java.util.ArrayList;
import java.util.List;

import botcord.components.channel.PMChannelSelector;
import botcord.components.message.DisMessagePanel;
import botcord.components.selector.ScreenSelector;
import botcord.event.BCListener;
import botcord.screen.util.BotCordScreen;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

public class PMScreen extends BotCordScreen {

    private JDA jda;
    private List<BCListener> listeners;
    private PMChannelSelector pmChannelSelector;
    private DisMessagePanel messagePanel;

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
        this.pmChannelSelector = new PMChannelSelector(new ArrayList<User>());
        this.messagePanel = new DisMessagePanel(null);
        this.listeners = new ArrayList<BCListener>();
    }

    private void getUsers() 
    {
        // Thread t = new Thread ( getUserRunnable() , "Getting Users");
        // t.setDaemon(true);
        // t.start();
        
	}

	// private Runnable getUserRunnable() 
    // {
    //     Runnable r = new Runnable()
    //     {
    //         @Override
    //         public void run() 
    //         {
    //             List<User> users = BCUtil.loadAllUsers();
    //             List<Guild> guilds = jda.getGuilds();
    //             for (Guild guild : guilds) 
    //             {
    //                 List<Member> members = guild.getMembers();
    //                 for (Member m : members) 
    //                 {
    //                     User u = m.getUser();
    //                     try 
    //                     {
    //                         u.openPrivateChannel().complete();
    //                         if (!users.contains(u))
    //                             users.add(u);
    //                     } catch (Exception ignored){
    //                     }    
    //                 }
    //             }
    //             pmChannelSelector.setUsers(users);
    //             UserCollection uc = new UserCollection(users);
    //             BCUtil.getCache().put(BCUtil.USER_CACHE, uc);
    //             update();
    //         }
    //     };
    //     return r;
    // }

    @Override
    public void config() 
    {
        getUsers();
        this.configSelector();
        this.getPanel().add(this.messagePanel);
    }

    @Override
    public void configSelector() 
    {
        updateBounds();
        super.configSelector();
        this.getPanel().add(this.pmChannelSelector);
    }

    private void updateBounds() 
    {
        updateSelectorBounds();
        updateChannelBounds();
        updateMessagePanelBounds();
    }

    private void updateMessagePanelBounds() 
    {
        int x = (int)( this.getWidth()  * SELECTOR_WIDTH) + 
                (int)( this.getWidth()  * CHANNEL_SELECTOR_WIDTH);
        int y = 0;
        int width  = this.getWidth() - x;
        int height = (int)( this.getHeight() * 1f);

        this.messagePanel.setBounds(x, y, width, height);
    }

    private void updateSelectorBounds() 
    {
        int x = 0;
        int y = 0;
        int width  = (int)( this.getWidth()  * SELECTOR_WIDTH);
        int height = (int)( this.getHeight() * 1f);

        this.getSelector().setBounds(x, y, width, height);
    }

    private void updateChannelBounds() 
    {
        int x = (int)( this.getWidth()  * SELECTOR_WIDTH);
        int y = 0;
        int width  = (int)( this.getWidth()  * CHANNEL_SELECTOR_WIDTH);
        int height = (int)( this.getHeight() * 1f);

        this.pmChannelSelector.setBounds(x, y, width, height);
    }

    @Override
    public void update() 
    {
        updateBounds();
        this.getSelector().update();
        this.pmChannelSelector.update();
    }

    public void setListeners(List<BCListener> listeners) 
    {
        this.listeners = listeners;
        getSelector().updateListeners(this.listeners);
        this.pmChannelSelector.updateListeners(this.listeners);
        this.messagePanel.updateListeners(this.listeners);
    }

    public List<BCListener> getListeners() 
    {
        return listeners;
    }

    public void addListener(BCListener l)
    {
        this.listeners.add(l);
        getSelector().updateListeners(this.listeners);
        this.pmChannelSelector.updateListeners(this.listeners);
        this.messagePanel.updateListeners(this.listeners);
    }
    
    public boolean rmListener(BCListener l)
    {
        boolean b = this.listeners.remove(l);

        getSelector().updateListeners(this.listeners);
        this.pmChannelSelector.updateListeners(this.listeners);
        this.messagePanel.updateListeners(this.listeners);

        return b;
    }

	public void setActiveChannel(PrivateChannel channel)
    {
        this.messagePanel.setChannel(channel);
	}
    
}
