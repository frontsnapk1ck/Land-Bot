package botcord.components.selector;

import java.util.List;

import botcord.components.gui.BotCordScrollPanel;
import botcord.event.BotCordListener;
import botcord.util.BotCordUtil;
import net.dv8tion.jda.api.JDA;

@SuppressWarnings("serial")
public class GuildSelector extends BotCordScrollPanel {

    private GuildSelectorPanel panel;

    public GuildSelector(JDA jda) 
    {
        this.panel = new GuildSelectorPanel(jda);
        init();
        config();
	}

    @Override
    public void init() 
    {
        this.setBackground(BotCordUtil.SCREEN_SELECTOR);
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
        panel.setSize(this.getWidth(), panel.getMaxH());
    }

    public void updateListeners(List<BotCordListener> listeners) 
    {
        this.panel.updateListeners(listeners);
	}

    
    
}
