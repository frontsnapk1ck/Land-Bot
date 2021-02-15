package botcord.components.channel;

import java.util.List;

import botcord.components.gui.BCScrollPanel;
import botcord.event.BCListener;
import botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.Guild;

@SuppressWarnings("serial")
public class ChannelSelector extends BCScrollPanel {

    private ChannelSelectorPanel panel;

    public ChannelSelector(Guild guild) 
    {
        this.panel = new ChannelSelectorPanel(guild);
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
        panel.setSize(this.getWidth(), panel.getMaxH());
        panel.update();
    }

	public void updateListeners(List<BCListener> listeners) 
    {
        this.panel.updateListeners(listeners);
	}

    
}
