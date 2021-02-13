package botcord.components.selector;

import botcord.components.util.BotCordScrollPanel;
import botcord.util.BotCordUtil;
import net.dv8tion.jda.api.entities.Guild;

@SuppressWarnings("serial")
public class ChannelSelector extends BotCordScrollPanel {

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
        panel.setSize(this.getWidth(), panel.getMaxH());
        panel.update();
    }

    
}
