package botcord.screen;

import java.util.ArrayList;
import java.util.List;

import botcord.collections.UserCollection;
import botcord.components.selector.PMChannelSelector;
import botcord.components.selector.ScreenSelector;
import botcord.event.BotCordListener;
import botcord.screen.util.BotCordScreen;
import botcord.util.BotCordUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

public class PMScreen extends BotCordScreen {

    private JDA jda;
    private List<BotCordListener> listeners;
    private PMChannelSelector pmChannelSelector;

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
        this.listeners = new ArrayList<BotCordListener>();
    }

    private void getUsers() 
    {
        Thread t = new Thread ( getUserRunnable() , "Getting Users");
        t.setDaemon(true);
        t.start();
	}

	private Runnable getUserRunnable() 
    {
        Runnable r = new Runnable()
        {
            @Override
            public void run() 
            {
                List<User> users = BotCordUtil.loadAllUsers();
                List<Guild> guilds = jda.getGuilds();
                for (Guild guild : guilds) 
                {
                    List<Member> members = guild.getMembers();
                    for (Member m : members) 
                    {
                        User u = m.getUser();
                        try 
                        {
                            u.openPrivateChannel().complete();
                            if (!users.contains(u))
                                users.add(u);
                        } catch (Exception ignored){
                        }    
                    }
                }
                pmChannelSelector.setUsers(users);
                UserCollection uc = new UserCollection(users);
                BotCordUtil.getCache().put(BotCordUtil.USER_CACHE, uc);
                update();
            }
        };
        return r;
    }

    @Override
    public void config() 
    {
        getUsers();
        this.configSelector();
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

	public void setActiveChannel(PrivateChannel data) 
    {
        System.out.println(data.getUser().getName());
	}
    
}
