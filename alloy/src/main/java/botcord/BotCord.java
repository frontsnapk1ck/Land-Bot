package botcord;

import botcord.event.DebugListener;
import botcord.manager.ScreenHolder;
import botcord.manager.ScreenInterface;
import botcord.manager.ScreenSwitchManager;
import botcord.util.BotCordColors;
import botcord.util.BotCordLinks;
import gui.ScreenFramework;
import gui.WindowFramework;
import net.dv8tion.jda.api.JDA;

public class BotCord extends WindowFramework implements BotCordColors, BotCordLinks, ScreenInterface {

    private static final long serialVersionUID = 2438097905045342324L;

    private JDA jda;
    private ScreenHolder screens;
    private ScreenSwitchManager manager;

    public BotCord(JDA jda) {
        this.jda = jda;
        configScreens();
        init();
    }

    private void configScreens() 
    {
        this.manager = new ScreenSwitchManager(this);
        this.screens = new ScreenHolder();
        this.screens.config(this.jda, this);
    }

    private void init() 
    {
        this.setSize(1600, 900);
        this.setCurrentScreen(screens.getDebugScreen());
        this.setVisible(true);
    }

    public void update() {
        this.screens.update();
        this.revalidate();
    }

    public DebugListener getDebugListener() {
        return this.screens.getDebugScreen();
    }

    @Override
    public void setScreen(ScreenFramework screen) {
        this.setCurrentScreen(screen);
    }

    @Override
    public ScreenSwitchManager getManager() 
    {
        return this.manager;
    }

}
