package botcord.components.selector;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import botcord.components.button.GuildButton;
import botcord.components.util.BotCordPanel;
import botcord.event.BotCordListener;
import botcord.util.BotCordUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

@SuppressWarnings("serial")
public class GuildSelectorPanel extends BotCordPanel {
    
    public static final float SCALE = 0.8f;

    private int maxH = 0;

    private JDA jda;
    private List<BotCordListener> listeners;
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
        this.listeners = new ArrayList<BotCordListener>();
        this.buttons = new ArrayList<GuildButton>();
        this.setBackground(BotCordUtil.SCREEN_SELECTOR);
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

    public void setListeners(List<BotCordListener> listeners) {
        this.listeners = listeners;
    }

    public List<BotCordListener> getListeners() {
        return listeners;
    }

    public void addListener(BotCordListener l)
    {
        this.listeners.add(l);
    }
    
    public boolean rmListener(BotCordListener l)
    {
        return this.listeners.remove(l);
    }

    public void updateListeners(List<BotCordListener> listeners) 
    {
        this.listeners = listeners;
        for (GuildButton guildButton : buttons) 
            guildButton.setListeners(this.listeners);
    }
    
    public int getMaxH() {
        return maxH;
    }

}
