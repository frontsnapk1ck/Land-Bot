package botcord.screens;

import java.util.ArrayList;
import java.util.List;

import botcord.BotCord;
import botcord.componets.selector.ChannelSelector;
import botcord.event.PressListener;
import botcord.manager.ScreenInterface;
import botcord.screens.util.AbstractMessageScreen;
import net.dv8tion.jda.api.entities.Guild;

public class GuildScreen extends AbstractMessageScreen {

    public static final float CHANNEL_WIDTH  = 0.15f;
    public static final float CHANNEL_HEIGHT = 1.00f;


    private Guild guild;
    private ChannelSelector channelSelector;

    public GuildScreen(Guild guild, ScreenInterface interf) 
    {
        super();
        this.guild = guild;
        setManager(interf.getManager());
        configSelector();
        configureChannelSlector();
        configPannel();
    }

    private void configureChannelSlector() 
    {
        this.channelSelector = new ChannelSelector(this.guild.getChannels());
        int x = this.getSelector().getWidth();
        int y = 0;
        int w = (int) (this.getWidth() * CHANNEL_WIDTH);
        int h = (int) (this.getHeight() * CHANNEL_HEIGHT);

        channelSelector.setBounds(x, y, w, h);
        this.getPanel().add(channelSelector);

    }

    private void configSelector() 
    {
        List<PressListener> listeners = new ArrayList<PressListener>();
        listeners.add(this.getManager());
        this.configSelector(listeners, this.guild.getJDA());
    }

    protected void configPannel() 
    {
        this.getPanel().setLayout(null);
        this.getPanel().add(this.getSelector());
        this.getPanel().setBackground(BotCord.BACKGROUND);
    }

    public Guild getGuild() {
        return guild;
    }

    @Override
    public void update() 
    {
        this.getSelector().update();
    }

    @Override
    public void onScreenResize() 
    {
        int w = (int) (this.getWidth() * (1f - SCREEN_WIDTH));
        int h = (int) (this.getHeight() * SCREEN_HEIGHT);

        int cw = (int) (this.getWidth() * CHANNEL_WIDTH);
        int ch = (int) (this.getHeight() * CHANNEL_HEIGHT);        

        this.getSelector().setBounds(0, 0, w, h);
        this.channelSelector.setBounds(w,0,cw,ch);

        this.update();
    }


}
