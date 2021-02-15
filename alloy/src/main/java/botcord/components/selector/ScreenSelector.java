package botcord.components.selector;

import java.util.List;

import botcord.components.gui.BCPanel;
import botcord.event.BCListener;
import botcord.util.BCUtil;
import net.dv8tion.jda.api.JDA;

@SuppressWarnings("serial")
public class ScreenSelector extends BCPanel {

	public static final float 		SPLIT 		= 0.20f;

	private JDA jda;
	private GuildSelector  guildSelector;
	private AlloySelector  alloySelector;

	public ScreenSelector(JDA jda) 
	{
		this.jda = jda;
		init();
		config();
		
	}

	@Override
	public void init() 
	{
		this.guildSelector = new GuildSelector(this.jda);
		this.alloySelector = new AlloySelector();
	}

	@Override
	public void config() 
	{
		updateBounds();
		this.setBackground(BCUtil.BACKGROUND);
		this.add(this.guildSelector);
		this.add(this.alloySelector);
		configTooltip();
	}

	private void configTooltip() 
    {
        this.setToolTipText("Screen Selector");
    }

	private void updateBounds() 
	{
		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int width = this.getWidth();
		int h1 = (int)(this.getHeight() * SPLIT);
		int h2 = (int)(this.getHeight() * (1f - SPLIT));
		int y2 = h1;

		this.alloySelector.setBounds(x1, y1, width, h1);
		this.guildSelector.setBounds(x2, y2, width, h2);
	}

	@Override
	public void update() 
	{
		updateBounds();
		this.alloySelector.update();
		this.guildSelector.update();
	}

	public void updateListeners(List<BCListener> listeners) 
	{
		this.alloySelector.updateListeners(listeners);
		this.guildSelector.updateListeners(listeners);
	}
    
}
