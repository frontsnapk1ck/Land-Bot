package frontsnapk1ck.botcord.components.selector;

import java.util.List;

import frontsnapk1ck.botcord.components.gui.BCScrollPanel;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.JDA;

public class GuildSelector extends BCScrollPanel {

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
        this.setBackground(BCUtil.SCREEN_SELECTOR);
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

    public void updateListeners(List<BCListener> listeners) 
    {
        this.panel.updateListeners(listeners);
	}

    
    
}
