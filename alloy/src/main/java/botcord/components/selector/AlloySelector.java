package botcord.components.selector;

import java.util.ArrayList;
import java.util.List;

import botcord.components.button.DebugButton;
import botcord.components.button.PMButton;
import botcord.components.util.BotCordPanel;
import botcord.event.BotCordListener;
import botcord.util.BotCordColors;

@SuppressWarnings("serial")
public class AlloySelector extends BotCordPanel {

    public static final float SCALE = 0.8f;

    private DebugButton debugButton;
    private PMButton pmButton;
    private List<BotCordListener> listeners;

    public AlloySelector() 
    {
        super();
        init();
        config();
    }

    @Override
    public void init() 
    {
        this.debugButton = new DebugButton();
        this.pmButton = new PMButton();
        this.listeners = new ArrayList<BotCordListener>();
        this.setBackground(BotCordColors.SCREEN_SELECTOR);
    }

    @Override
    public void config() 
    {
        this.configTooltip();
        this.addButtons();
    }

    private void addButtons() 
    {
        updateButtons();
        this.add(debugButton);
        this.add(pmButton);
    }

    private void updateButtons() 
    {
        int w = (int)(this.getWidth() * SCALE);
        int h = w;
        int x = (int)(this.getWidth() * (1f - SCALE) / 2);
        int y = x;
        pmButton.setBounds(x, y, w, h);
        y += this.getWidth();
        debugButton.setBounds(x, y, w, h);
    }

    private void configTooltip() 
    {
        this.setToolTipText("Alloy Selector");
    }

    @Override
    public void update() 
    {
        updateButtons();
        this.pmButton.update();
        this.debugButton.update();
    }

    public void updateListeners(List<BotCordListener> listeners) 
    {
        this.listeners = listeners;
        this.pmButton.setListeners(this.listeners);
        this.debugButton.setListeners(this.listeners);
	}
    
}
