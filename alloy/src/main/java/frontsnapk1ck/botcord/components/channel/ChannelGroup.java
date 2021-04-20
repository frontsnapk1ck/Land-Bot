package frontsnapk1ck.botcord.components.channel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import frontsnapk1ck.botcord.components.gui.BCPanel;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.GuildChannel;

public class ChannelGroup extends BCPanel {

    public static final int COMP_HEIGHT = 30;

    private Type type;
    private Category category;
    private List<GuildChannel> list;
    private String name;
    
    private List<BCListener> listeners;
    private List<ChannelButton> buttons;
    private JLabel label;


    public ChannelGroup(Category category) 
    {
        this.category = category;
        
        this.type = Type.CATEGORIZED;
        init();
        config();
	}

    public ChannelGroup(List<GuildChannel> missing) 
    {
        this.list = missing;

        this.type = Type.UNCATEGORIZED;
        init();
        config();
	}

    @Override
    public void init() 
    {
        this.buttons = new ArrayList<ChannelButton>();
        this.label = new JLabel();
        this.add(this.label);

        if (this.isCategorized())
            initCategorized();
        else
            initUncategorized();
    }

    private void initUncategorized() 
    {
        for (GuildChannel c : list) 
            this.buttons.add(new ChannelButton(c) );
        this.name = "Uncategorized";
    }

    private void initCategorized() 
    {
        this.list = this.category.getChannels();
        for (GuildChannel c : list) 
            this.buttons.add(new ChannelButton(c) );
        this.name = this.category.getName();
    }

    private boolean isCategorized() 
    {
        return this.getType().equals(Type.CATEGORIZED);
    }

    @Override
    public void config() 
    {
        this.setBackground(null);
        for (ChannelButton channelButton : buttons) 
            this.add(channelButton);
        this.configLabel();
        this.configToolTip();
        updateBounds();
    }

    private void configLabel() 
    {
        this.label.setText(this.name);
        this.label.setForeground(BCUtil.TEXT);
    }

    private void configToolTip() 
    {
        this.setToolTipText(this.name);
    }

    private void updateBounds() 
    {
        int offset = (int)(this.getWidth() * 0.05);
        this.label.setBounds(offset,0,this.getWidth(),COMP_HEIGHT);
        int i = 1;
        for (ChannelButton channelButton : buttons) 
        {
            int x = 0;
            int y = i * COMP_HEIGHT;
            int w = this.getWidth();
            int h = COMP_HEIGHT;
            channelButton.setBounds(x,y,w,h);

            i++;
        }
    }

    public void updateListeners(List<BCListener> listeners) 
    {
        this.listeners = listeners;
        for (ChannelButton cb : this.buttons)
            cb.setListeners(listeners);
    }
    
    public List<BCListener> getListeners() {
        return listeners;
    }

    @Override
    public void update() 
    {
        updateBounds();
    }

    public Type getType() {
        return type;
    }

    public int getNumComps()
    {
        return this.buttons.size() + 1;
    }

    public List<ChannelButton> getButtons() 
    {
        return buttons;
    }

    public JLabel getLabel() {
        return label;
    }

    public enum Type
    {
        CATEGORIZED,
        UNCATEGORIZED
    }
    
}
