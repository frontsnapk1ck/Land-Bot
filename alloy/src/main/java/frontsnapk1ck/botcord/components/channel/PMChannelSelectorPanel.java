package frontsnapk1ck.botcord.components.channel;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.botcord.components.gui.BCPanel;
import frontsnapk1ck.botcord.components.gui.base.BaseBCLabel;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.User;

public class PMChannelSelectorPanel extends BCPanel {

    public static final int COMP_HEIGHT = 30;
    public static final int BATCH_SIZE = 50;

    private List<User> users;

    private List<PMChannelButton> buttons;

    private List<BCListener> listeners;

    private BaseBCLabel tmpLabel;

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
        this.setBackground(BCUtil.CHANNEL_SELECTOR);
    }

    @Override
    public void config() 
    {
        this.configToolTip();
        this.configButtons();
        this.updateBounds();
        this.updateLayout();
        this.setTempText();
    }

    private void setTempText() 
    {
        BaseBCLabel label = new BaseBCLabel();
        label.setForeground(BCUtil.TEXT);
        label.setText("<html>WORKING ON IT<P/>please dont touch anything</html>");
        this.tmpLabel = label;
        this.add(label);
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
        int size = this.users.size() == 0 ? 1 : this.users.size();
        this.setLayout(new GridLayout(size,1,0,10));
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
        this.remove(this.tmpLabel);
        this.users = users;
        this.updateLayout();
        for (PMChannelButton b : this.buttons )
            this.remove(b);

        this.buttons = new ArrayList<PMChannelButton>();
        
        for (User u : this.users) 
        {
            PMChannelButton b = new PMChannelButton(u.openPrivateChannel().complete());
            this.buttons.add(b);
            b.setListeners(this.listeners);
            this.add(b);
        }
	}

	public void setListeners(List<BCListener> listeners) 
    {
        this.listeners = listeners;
        for (PMChannelButton b : this.buttons)
            b.setListeners(listeners);
	}
    
}
