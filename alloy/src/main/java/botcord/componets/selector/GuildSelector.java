package botcord.componets.selector;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;

import botcord.BotCord;
import botcord.componets.GuildButton;
import botcord.event.PressListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

@SuppressWarnings("serial")
public class GuildSelector extends JScrollPane {

    private JDA jda;
    private List<GuildButton> buttons;
    private List<PressListener> listeners;

    public GuildSelector(JDA jda) 
    {
        init();
        this.jda = jda;
        configButtons();
        configScrollBar();
	}

    private void init() 
    {
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.buttons = new ArrayList<GuildButton>();
        this.listeners = new ArrayList<PressListener>();
        this.setBackground(BotCord.BACKGROUND);

        this.setToolTipText("Guild Selector");
    }

    private void configScrollBar() 
    {
        this.add(this.verticalScrollBar);
    }

	private void configButtons() 
    {
        //TODO PM options
        List<Guild> guilds = this.jda.getGuilds();
        for (int i = 0; i < guilds.size(); i++) 
        {
            Guild g = guilds.get(i);
            int w = (int) (this.getWidth() * .7f);
            int h = w;
            GuildButton button = new GuildButton(g);
            int x = (int) (this.getWidth() * .15f);
            int y = this.getWidth() * i;
            button.setBounds(x, y, w, h);
            button.updateImage(w,h);
            for (PressListener l : this.listeners)
                button.add(l);
            this.add(button);
            this.buttons.add(button);
        }
    }

    public void update() 
    {
        boolean nonNull = this.buttons.size() != 0;
        boolean fullUpdate = this.buttons.size() != this.jda.getGuilds().size() || 
                (nonNull && this.buttons.get(0).getWidth() == 0);
        if (fullUpdate)
        {
            clearButtons();
            configButtons();
        }

        int w = (int) (this.getWidth() * .7f);
        int h = w;
        for (GuildButton button : buttons) 
            button.update(w, h);
	}

    private void clearButtons() 
    {
        for (GuildButton guildButton : buttons) 
            this.remove(guildButton);
        this.buttons = new ArrayList<GuildButton>();
    }

    public void setActionListeners(List<PressListener> ls) 
    {
        this.listeners = ls;
	}
    
}
