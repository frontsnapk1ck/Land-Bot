package botcord.components.message.log;

import java.util.List;

import botcord.components.gui.BCScrollPanel;
import botcord.event.BCListener;
import botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.MessageHistory;

@SuppressWarnings("serial")
public class DisMessageLog extends BCScrollPanel {

    private DisMessageLogPanel panel;

    public DisMessageLog(MessageHistory history) 
    {
        super();
        this.panel = new DisMessageLogPanel(history);
        init();
        config();
    }

    @Override
    public void init() 
    {
        this.setBackground(BCUtil.BACKGROUND);
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

	public void updateListeners(List<BCListener> listeners) 
    {
        this.panel.updateListeners(listeners);
	}


    
}
