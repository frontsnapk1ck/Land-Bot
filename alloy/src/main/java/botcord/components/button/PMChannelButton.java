package botcord.components.button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import botcord.components.util.BotCordButton;
import botcord.event.BotCordListener;
import botcord.event.PressEvent;
import botcord.event.SwitchTarget;
import botcord.util.BotCordUtil;
import net.dv8tion.jda.api.entities.PrivateChannel;

@SuppressWarnings("serial")
public class PMChannelButton extends BotCordButton {

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
        this.setForeground(BotCordUtil.TEXT);
		this.configToolTip();
		configListener();
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
    protected void configListener() 
    {
        this.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ignored) 
			{
				PressEvent e = new PressEvent(SwitchTarget.PRIVATE_CHANNEL);
				e.setData(getChannel());
				for (BotCordListener l : getListeners())
					l.onPress(e);
			}
		});
    }

    public PrivateChannel getChannel() 
    {
        return channel;
    }
    
}
