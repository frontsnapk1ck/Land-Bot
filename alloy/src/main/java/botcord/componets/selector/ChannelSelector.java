package botcord.componets.selector;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import botcord.componets.ChannelButton;
import botcord.util.GuiUtil;
import net.dv8tion.jda.api.entities.GuildChannel;

@SuppressWarnings("serial")
public class ChannelSelector extends JPanel {

    private List<ChannelButton> buttons;

    public ChannelSelector(List<GuildChannel> list) 
    {
        init();
        configChannels(list);
	}

    private void configChannels(List<GuildChannel> list) 
    {
        for (GuildChannel guildChannel : list) 
        {
            this.buttons.add(new ChannelButton(guildChannel));
        }
    }

    private void init() 
    {
        this.buttons = new ArrayList<ChannelButton>();
        this.setBackground(GuiUtil.randomColor());
        this.setToolTipText("Channel Selector");
    }
    
}
