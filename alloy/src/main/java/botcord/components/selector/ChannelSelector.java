package botcord.components.selector;

import java.util.ArrayList;
import java.util.List;

import botcord.components.ChannelGroup;
import botcord.components.util.BotCordPanel;
import botcord.event.BotCordListener;
import botcord.util.BotCordColors;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import utility.Util;

@SuppressWarnings("serial")
public class ChannelSelector extends BotCordPanel {

    private Guild guild;
    private ArrayList<BotCordListener> listeners;

    public ChannelSelector(Guild guild) 
    {
        this.guild = guild;
        init();
        config();
	}

    @Override
    public void init() 
    {
        this.listeners = new ArrayList<BotCordListener>();
        this.setBackground(BotCordColors.CHANNEL_SELECTOR);
    }

    @Override
    public void config() 
    {
        this.configToolTip();
        this.makeGroups();
    }

    private void makeGroups() 
    {
        List<Category> catagories = this.guild.getCategories();
        List<GuildChannel> categorized = new ArrayList<GuildChannel>();
        List<GuildChannel> missing = new ArrayList<GuildChannel>();
        Util.copy( missing ,this.guild.getChannels());
        List<ChannelGroup> channelGroups = new ArrayList<ChannelGroup>();
        for (Category category : catagories) 
        {
            categorized.addAll(category.getChannels());
            channelGroups.add(new ChannelGroup(category));
        }
        missing.removeAll(categorized);
        channelGroups.add(new ChannelGroup(missing));
        
    }


    private void configToolTip() 
    {
        this.setToolTipText("Channel Selector");
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }
    
}
