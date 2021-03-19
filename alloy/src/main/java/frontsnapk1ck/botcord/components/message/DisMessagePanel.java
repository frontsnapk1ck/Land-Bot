package frontsnapk1ck.botcord.components.message;

import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.botcord.components.gui.BCPanel;
import frontsnapk1ck.botcord.components.message.log.DisMessageLog;
import frontsnapk1ck.botcord.event.BCListener;
import net.dv8tion.jda.api.entities.MessageChannel;

@SuppressWarnings("serial")
public class DisMessagePanel extends BCPanel {

    public static final float SPLIT = .90f;

    private boolean init;
    private MessageChannel channel;
    
    private DisMessageLog messageLog;

    private List<BCListener> listeners;


    public DisMessagePanel(MessageChannel channel) 
    {
        super();
        this.channel = channel;
        if (channel == null)
            return;
        init();
        config();
        this.init = true;
    }

    @Override
    public void init() 
    {
        this.listeners = new ArrayList<BCListener>();
        this.messageLog = new DisMessageLog(channel.getHistory());
        this.messageLog.updateListeners(this.listeners);
    }

    @Override
    public void config() 
    {
        this.add(this.messageLog);
        this.updateLayout();
    }

    private void updateLayout() 
    {
        setMessageLogBounds();
    }

    private void setMessageLogBounds() 
    {
        int x = 0;
        int y = 0;
        int w = this.getWidth();
        int h = (int)(this.getHeight() * SPLIT);
        this.messageLog.setBounds(x, y, w, h);
    }

    @Override
    public void update() 
    {
        this.updateLayout();
        this.messageLog.update();
    }

    public void setChannel(MessageChannel channel) 
    {
        this.channel = channel;
        if (!this.init && channel != null)
        {
            this.init();
            this.config();
            this.init = true;
        }
        else if (channel != null)
        {
            this.remove(this.messageLog);
            this.messageLog = new DisMessageLog(channel.getHistory());
            this.messageLog.updateListeners(this.listeners);
            this.add(this.messageLog);
            updateLayout();
        }

    }

    public MessageChannel getChannel() {
        return channel;
    }

	public void updateListeners(List<BCListener> listeners) 
    {
        this.listeners = listeners;
        if (this.messageLog != null)
            this.messageLog.updateListeners(listeners);
	}
    
}
