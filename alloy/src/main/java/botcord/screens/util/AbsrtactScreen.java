package botcord.screens.util;

import java.util.List;

import botcord.componets.selector.ScreenSelector;
import botcord.event.PressListener;
import botcord.manager.ScreenSwitchManager;
import gui.ScreenFramework;
import net.dv8tion.jda.api.JDA;

public abstract class AbsrtactScreen extends ScreenFramework {

    public static final float SCREEN_WIDTH = .95f;
    public static final float SCREEN_HEIGHT = 1.00f;

    private ScreenSelector selector;
    private ScreenSwitchManager manager;

    protected void configSelector(List<PressListener> ls, JDA jda)
    {
        this.setSelector(new ScreenSelector(jda));
        this.getSelector().setActionListeners(ls);
    }

    public abstract void update();

    protected void configPannel()
    {
        this.getPanel().setLayout(null);
        this.getPanel().add(this.getSelector());
    }

    public ScreenSelector getSelector() {
        return selector;
    }

    public void setSelector(ScreenSelector selector) 
    {
        this.selector = selector;
    }

    public ScreenSwitchManager getManager() {
        return manager;
    }

    public void setManager(ScreenSwitchManager manager) {
        this.manager = manager;
    }
}
