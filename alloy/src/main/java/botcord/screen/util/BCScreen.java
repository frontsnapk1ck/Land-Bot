package botcord.screen.util;

import botcord.components.selector.ScreenSelector;
import botcord.util.BCComponent;
import gui.ScreenFramework;

public abstract class BCScreen extends ScreenFramework implements BCComponent {

    public static final float       SELECTOR_WIDTH              = 0.05f;
    public static final float       CHANNEL_SELECTOR_WIDTH      = 0.15f;

    private ScreenSelector selector;

    public BCScreen() 
    {
        super();
    }

    public void configSelector()
    {
        this.getPanel().add(this.getSelector());
    }

    public ScreenSelector getSelector() 
    {
        return selector;
    }

    public void setSelector(ScreenSelector selector) 
    {
        this.selector = selector;
    }

    
}
