package frontsnapk1ck.botcord.components.selector;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.botcord.components.gui.BCPanel;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public class GuildSelectorPanel extends BCPanel {
    
    public static final float SCALE = 0.8f;

    private int maxH = 0;

    private JDA jda;
    private List<BCListener> listeners;
    private List<GuildButton> buttons;

    public GuildSelectorPanel(JDA jda) 
    {
        this.jda = jda;
        init();
        config();
	}

    @Override
    public void init() 
    {
        this.listeners = new ArrayList<BCListener>();
        this.buttons = new ArrayList<GuildButton>();
        this.setBackground(BCUtil.SCREEN_SELECTOR);
    }

    @Override
    public void config() 
    {
        this.setLayout(new GridLayout(this.jda.getGuilds().size(),1,0,10));
        this.configTooltip();
        addButtons();
    }

    private void addButtons() 
    {
        List<Guild> guilds = this.jda.getGuilds();

        // int i = 0;
        for (Guild guild : guilds) 
        {
            GuildButton button = new GuildButton(guild);
            int w = (int)(this.getWidth() * SCALE);
            int h = w;
            // int x = (int)(this.getWidth() * (1f - SCALE) / 2);
            // int y = i * this.getWidth();

            button.setSize(w, h);
            // button.setBounds(x, y, w, h);
            button.setListeners(this.getListeners());

            this.buttons.add(button);
            this.add(button);

            // i++;
        }
    }

    private void configTooltip() 
    {
        this.setToolTipText("Guild Selector");
    }

    @Override
    public void update() 
    {
        List<Guild> guilds = this.jda.getGuilds();
        if (guilds.size() != this.buttons.size())
            resetButtons();
        else    
            updateButtons();
    }

    private void updateButtons() 
    {
        this.removeAll();
        // int i = 0;
        for (GuildButton button : this.buttons) 
        {
            button = new GuildButton(button.getGuild());
            int w = (int)(this.getWidth() * SCALE);
            int h = w;
            // int x = (int)(this.getWidth() * (1f - SCALE) / 2);
            // int y = i * this.getWidth();

            // if (y > this.maxH)
            //     this.maxH = y;

            // button.setBounds(x, y, w, h);
            button.setSize(w, h);
            button.setListeners(this.getListeners());
            button.update();
            this.add(button);
            
            // i++;
        }
    }

    private void resetButtons() 
    {
        for (GuildButton guildButton : buttons) 
            this.remove(guildButton);
        this.buttons.clear();
        this.addButtons();
    }

    public void setListeners(List<BCListener> listeners) {
        this.listeners = listeners;
    }

    public List<BCListener> getListeners() {
        return listeners;
    }

    public void addListener(BCListener l)
    {
        this.listeners.add(l);
    }
    
    public boolean rmListener(BCListener l)
    {
        return this.listeners.remove(l);
    }

    public void updateListeners(List<BCListener> listeners) 
    {
        this.listeners = listeners;
        for (GuildButton guildButton : buttons) 
            guildButton.setListeners(this.listeners);
    }
    
    public int getMaxH() {
        return maxH;
    }

}
