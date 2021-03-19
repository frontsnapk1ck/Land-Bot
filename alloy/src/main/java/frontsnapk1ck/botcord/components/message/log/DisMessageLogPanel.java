package frontsnapk1ck.botcord.components.message.log;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.botcord.components.gui.BCPanel;
import frontsnapk1ck.botcord.components.message.message.DisMessage;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;

@SuppressWarnings("serial")
public class DisMessageLogPanel extends BCPanel {

    public static final int     BATCH_SIZE      = 50;

    private MessageHistory history;

    private List<DisMessage> disMessages;

    private List<BCListener> listeners;

    public DisMessageLogPanel(MessageHistory history) 
    {
        super();
        this.history = history;
        init();
        config();
    }

    @Override
    public void init() 
    {
        this.disMessages = new ArrayList<DisMessage>();
        this.setBackground(BCUtil.BACKGROUND);
    }

    @Override
    public void config() 
    {
        configToolTip();
        makeMessages();
        configMessages();
        updateLayout();
    }

    private void updateLayout() 
    {
        this.setLayout( new GridLayout(this.disMessages.size(),1,0,0));
    }

    private void configMessages()
    {
        for (DisMessage disMessage : disMessages) 
            this.add(disMessage);
    }

    private void configToolTip() 
    {
        this.setToolTipText("Message Log");
    }

    private void makeMessages()
    {
        List<Message> messages = this.history.retrievePast(BATCH_SIZE).complete();
        for (Message m : messages) 
            this.disMessages.add(new DisMessage(m));
    }

    @Override
    public void update() 
    {
        for (DisMessage disMessage : disMessages) 
        {
            disMessage.update();
        }
    }

	public void updateListeners(List<BCListener> listeners) 
    {
        this.listeners = listeners;
        for (DisMessage m : disMessages)
            m.setListeners(this.listeners);
	}
    
}
