package frontsnapk1ck.botcord.screen;

import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.botcord.components.channel.ChannelSelector;
import frontsnapk1ck.botcord.components.member.MemberList;
import frontsnapk1ck.botcord.components.message.DisMessagePanel;
import frontsnapk1ck.botcord.components.selector.ScreenSelector;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.screen.util.BotCordScreen;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class GuildScreen extends BotCordScreen {

    public static final float       CHANNEL_SELECTOR_WIDTH              = 0.15f;
    public static final float       MEMBER_LIST_WIDTH                   = 0.15f;

    private Guild guild;
    private List<BCListener> listeners;
    private ChannelSelector channelSelector;
    private DisMessagePanel messagePanel;
    private MemberList memberList;

    public GuildScreen(Guild guild) 
    {
        super();
        this.guild = guild;
        this.init();
        this.config();
    }

    @Override
    protected void onScreenResize() 
    {
        update();    
    }

    @Override
    public void init() 
    {
        this.setSelector(new ScreenSelector(this.guild.getJDA()));
        this.channelSelector = new ChannelSelector(this.guild);
        this.messagePanel = new DisMessagePanel(this.guild.getDefaultChannel());
        this.memberList = new MemberList(this.guild);
        this.listeners = new ArrayList<BCListener>();
    }

    @Override
    public void config() 
    {
        this.configSelector();
        this.getPanel().add(this.memberList);
        this.getPanel().add(this.messagePanel);
    }

	@Override
    public void configSelector() 
    {
        updateBounds();
        super.configSelector();
        this.getPanel().add(this.channelSelector);

    }

    private void updateBounds() 
    {
        updateSelectorBounds();
        updateChannelBounds();
        updateMessagePanelBounds();
        updateMemberBounds();
    }

    private void updateMessagePanelBounds() 
    {
        int x = (int)( this.getWidth()  * SELECTOR_WIDTH) + 
                (int)( this.getWidth()  * CHANNEL_SELECTOR_WIDTH);
        int y = 0;
        int maxOff = (int)( this.getWidth()  * MEMBER_LIST_WIDTH );
        int width  = this.getWidth() - x - maxOff;
        int height = (int)( this.getHeight() * 1f);

        this.messagePanel.setBounds(x, y, width, height);
    }

    private void updateMemberBounds() 
    {
        int width  = (int)( this.getWidth()  * MEMBER_LIST_WIDTH );
        int height = (int)( this.getHeight() * 1f);
        int x = (int)( this.getWidth()  - width );
        int y = 0;

        this.memberList.setBounds(x, y, width, height);
    }

    private void updateSelectorBounds() 
    {
        int x = 0;
        int y = 0;
        int width  = (int)( this.getWidth()  * SELECTOR_WIDTH);
        int height = (int)( this.getHeight() * 1f);

        this.getSelector().setBounds(x, y, width, height);
    }

    private void updateChannelBounds() 
    {
        int x = (int)( this.getWidth()  * SELECTOR_WIDTH);
        int y = 0;
        int width  = (int)( this.getWidth()  * CHANNEL_SELECTOR_WIDTH);
        int height = (int)( this.getHeight() * 1f);

        this.channelSelector.setBounds(x, y, width, height);
    }

    @Override
    public void update() 
    {
        updateBounds();
        this.getSelector().update();
        this.channelSelector.update();
        this.memberList.update();
        this.messagePanel.update();
    }
    
    public void setListeners(List<BCListener> listeners) 
    {
        this.listeners = listeners;

        getSelector().updateListeners(this.listeners);
        this.channelSelector.updateListeners(this.listeners);
        this.messagePanel.updateListeners(this.listeners);

    }

    public List<BCListener> getListeners() 
    {
        return listeners;
    }

    public void addListener(BCListener l)
    {
        this.listeners.add(l);

        getSelector().updateListeners(this.listeners);
        this.channelSelector.updateListeners(this.listeners);
        this.messagePanel.updateListeners(this.listeners);

    }
    
    public boolean rmListener(BCListener l)
    {
        boolean b = this.listeners.remove(l);
        
        getSelector().updateListeners(this.listeners);
        this.channelSelector.updateListeners(this.listeners);
        this.messagePanel.updateListeners(this.listeners);

        return b;
    }

    public void setActiveChannel(GuildChannel channel) 
    {
        if (channel instanceof TextChannel)
            this.messagePanel.setChannel((TextChannel)channel);
	}
}
