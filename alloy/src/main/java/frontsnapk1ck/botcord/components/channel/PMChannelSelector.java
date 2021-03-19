package frontsnapk1ck.botcord.components.channel;

import java.util.List;

import frontsnapk1ck.botcord.components.gui.BCScrollPanel;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.User;

@SuppressWarnings("serial")
public class PMChannelSelector extends BCScrollPanel {

    private PMChannelSelectorPanel panel;
    private List<BCListener> listeners;

    public PMChannelSelector(List<User> users) 
    {
        super();
        this.panel = new PMChannelSelectorPanel(users);
        init();
        config();
    }

    @Override
    public void init() 
    {
        this.setBackground(BCUtil.CHANNEL_SELECTOR);
    }

    @Override
    public void config() 
    {
        this.setViewportView(this.panel);
    }

    @Override
    public void update() 
    {
        panel.setBounds(this.getBounds());
        panel.update();
    }

	public List<User> getUsers() 
    {
        return this.panel.getUsers();
	}

	public void setUsers(List<User> users) 
    {
        this.panel.setUsers(users);
	}

	public void updateListeners(List<BCListener> listeners) 
    {
        this.listeners = listeners;
        this.panel.setListeners(listeners);
	}
    
    public List<BCListener> getListeners() 
    {
        return listeners;
    }
}
