package frontsnapk1ck.botcord.components.channel;

import java.util.Set;

import frontsnapk1ck.botcord.components.gui.BCButton;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.event.PressEvent;
import frontsnapk1ck.botcord.event.SwitchTarget;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.GuildChannel;

@SuppressWarnings("serial")
public class ChannelButton extends BCButton {

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
		this.setForeground(BCUtil.TEXT);
		this.configToolTip();
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
	protected void onRightClick(Set<MouseModifiers> modifiers) 
	{
		PressEvent e = new PressEvent(SwitchTarget.CHANNEL);
		e.setData(getChannel());
		for (BCListener l : getListeners())
			l.onPress(e);
	}

	public GuildChannel getChannel() 
	{
		return channel;
	}


    
}
