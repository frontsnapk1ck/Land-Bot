package botcord.components.channel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import botcord.components.gui.BotCordButton;
import botcord.event.BotCordListener;
import botcord.event.PressEvent;
import botcord.event.SwitchTarget;
import botcord.util.BotCordUtil;
import net.dv8tion.jda.api.entities.GuildChannel;

@SuppressWarnings("serial")
public class ChannelButton extends BotCordButton {

	private GuildChannel channel;

	public ChannelButton(GuildChannel c) 
	{
		this.channel = c;
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
		this.setText(this.channel.getName());
	}

	private void configToolTip() 
	{
		this.setToolTipText(this.channel.getName());
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
				PressEvent e = new PressEvent(SwitchTarget.CHANNEL);
				e.setData(getChannel());
				for (BotCordListener l : getListeners())
					l.onPress(e);
			}
		});
	}

	public GuildChannel getChannel() 
	{
		return channel;
	}


    
}
