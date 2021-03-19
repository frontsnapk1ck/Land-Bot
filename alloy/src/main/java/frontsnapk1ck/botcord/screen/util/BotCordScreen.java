package frontsnapk1ck.botcord.screen.util;

import frontsnapk1ck.botcord.components.selector.ScreenSelector;
import frontsnapk1ck.botcord.util.BotCordComponent;
import frontsnapk1ck.gui.ScreenFramework;

public abstract class BotCordScreen extends ScreenFramework implements BotCordComponent {

    public static final float       SELECTOR_WIDTH              = 0.05f;
    public static final float       CHANNEL_SELECTOR_WIDTH      = 0.15f;

    private ScreenSelector selector;

    public BotCordScreen() 
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
