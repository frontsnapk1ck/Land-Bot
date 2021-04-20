package frontsnapk1ck.botcord.components.selector;

import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.botcord.components.gui.BCPanel;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.util.BCUtil;

public class AlloySelector extends BCPanel {

    public static final float SCALE = 0.8f;

    private DebugButton debugButton;
    private PMButton pmButton;
    private List<BCListener> listeners;

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
        this.listeners = new ArrayList<BCListener>();
        this.setBackground(BCUtil.SCREEN_SELECTOR);
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

    public void updateListeners(List<BCListener> listeners) 
    {
        this.listeners = listeners;
        this.pmButton.setListeners(this.listeners);
        this.debugButton.setListeners(this.listeners);
	}
    
}
