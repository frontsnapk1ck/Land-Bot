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
    private List<ChannelGroup> groups;
    private List<BotCordListener> listeners;

    public ChannelSelector(Guild guild) 
    {
        this.guild = guild;
        init();
        config();
	}

    @Override
    public void init() 
    {
        this.setBackground(BotCordColors.CHANNEL_SELECTOR);
    }

    @Override
    public void config() 
    {
        this.configToolTip();
        this.makeGroups();
        this.configGroups();
        this.updateBounds();
    }

    private void configGroups() 
    {
        for (ChannelGroup channelGroup : groups)
            this.add(channelGroup);
    }

    private void updateBounds() 
    {
        int y = 0;
        int x = 0;
        int w = this.getWidth();
        for (ChannelGroup cg : groups) 
        {
            int h = cg.getNumComps() * ChannelGroup.COMP_HEIGHT;
            cg.setBounds(x, y, w, h);
            y += h;
        }
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
        for (GuildChannel c : missing) 
        {
            if (c instanceof Category)
                categorized.add(c);
        }
        missing.removeAll(categorized);
        channelGroups.add(new ChannelGroup(missing));
        this.groups = channelGroups;
    }


    private void configToolTip() 
    {
        this.setToolTipText("Channel Selector");
    }

    @Override
    public void update() 
    {
        updateBounds();
        for (ChannelGroup channelGroup : groups) 
            channelGroup.update();
    }

    public void updateListeners(List<BotCordListener> listeners) 
    {
        this.listeners = listeners;
        for (ChannelGroup g : this.groups)
            g.updateListeners(listeners);
    }
    
    public List<BotCordListener> getListeners() {
        return listeners;
    }
    
}
