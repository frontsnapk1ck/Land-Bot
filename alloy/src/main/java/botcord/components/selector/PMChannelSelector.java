package botcord.components.selector;

import java.util.List;

import botcord.components.util.BotCordScrollPanel;
import botcord.util.BotCordUtil;
import net.dv8tion.jda.api.entities.User;

@SuppressWarnings("serial")
public class PMChannelSelector extends BotCordScrollPanel {

    private PMChannelSelectorPanel panel;

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
        this.setBackground(BotCordUtil.CHANNEL_SELECTOR);
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
    
}
