package botcord.components.channel;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import botcord.components.gui.BotCordPanel;
import botcord.util.BotCordUtil;
import net.dv8tion.jda.api.entities.User;

@SuppressWarnings("serial")
public class PMChannelSelectorPanel extends BotCordPanel {

    public static final int COMP_HEIGHT = 30;

    private List<User> users;

    private List<PMChannelButton> buttons;

    public PMChannelSelectorPanel(List<User> users) 
    {
        super();
        this.users = users;
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
        this.configToolTip();
        this.configButtons();
        this.updateBounds();
        this.updateLayout();
    }

    private void configButtons() 
    {
        this.buttons = new ArrayList<PMChannelButton>();
        for (User u : this.users) 
        {
            PMChannelButton b = new PMChannelButton(u.openPrivateChannel().complete());
            this.buttons.add(b);
            this.add(b);
        }
    }

    private void configToolTip() 
    {
        this.setToolTipText("Private Channel Selector");
    }

    private void updateBounds() 
    {
        int w = this.getWidth();
        int h = COMP_HEIGHT;
        for (PMChannelButton u : this.buttons) 
            u.setSize( w, h );
    }

    private void updateLayout() 
    {
        this.setLayout(new GridLayout(this.users.size(),1,0,10));
    }

    @Override
    public void update() 
    {
        updateBounds();
        for (PMChannelButton pmChannelButton : buttons)
            pmChannelButton.update();
    }

	public List<User> getUsers() 
    {
        return this.users;
	}

	public void setUsers(List<User> users) 
    {
        this.users = users;
        for (PMChannelButton b : this.buttons )
            this.remove(b);

        this.buttons = new ArrayList<PMChannelButton>();
        
        for (User u : this.users) 
        {
            PMChannelButton b = new PMChannelButton(u.openPrivateChannel().complete());
            this.buttons.add(b);
        }

        for (PMChannelButton b : this.buttons )
            this.add(b);
	}
    
}
