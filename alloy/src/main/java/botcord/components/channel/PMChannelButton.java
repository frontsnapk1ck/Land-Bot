package botcord.components.channel;

import java.util.Set;

import botcord.components.gui.BCButton;
import botcord.event.BCListener;
import botcord.event.PressEvent;
import botcord.event.SwitchTarget;
import botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.PrivateChannel;

@SuppressWarnings("serial")
public class PMChannelButton extends BCButton {

    private PrivateChannel channel;

    public PMChannelButton(PrivateChannel pc) 
    {
        super();
        this.channel = pc;
        init();
        config();
    }

    @Override
    public void init() 
    {
		this.setBackground(null);
		this.setBorder(null);
    }

    @Override
    public void config() 
    {
        this.setForeground(BCUtil.TEXT);
		this.configToolTip();
		configText();
    }

    private void configText() 
    {
		this.setText(this.channel.getUser().getAsTag());
    }

    private void configToolTip() 
    {
        this.setToolTipText(this.channel.getUser().getAsTag());
    }

    @Override
    public void update() 
    {
        //nothing to do yet
    }

    @Override
    protected void onRightClick(Set<MouseModifiers> modifiers) 
    {
        PressEvent e = new PressEvent(SwitchTarget.PRIVATE_CHANNEL);
        e.setData(getChannel());
        for (BCListener l : getListeners())
            l.onPress(e);
    }

    public PrivateChannel getChannel() 
    {
        return channel;
    }
    
}
