package botcord.manager;

import botcord.BotCord;
import botcord.manager.util.SwitchEvent;
import botcord.manager.util.ScreenProxy;
import botcord.manager.util.Switchable;
import botcord.screen.DebugScreen;
import botcord.screen.GuildScreen;
import botcord.screen.PMScreen;
import gui.ScreenFramework;

public class ScreenManager implements Switchable, ScreenProxy {

    private DebugScreen debugScreen;
    private ScreenSwitchManager manager;
    private BotCord main;

    public ScreenManager(BotCord botCord) 
    {
        this.main = botCord;
        init();
        config();
	}

    private void config() 
    {
        this.debugScreen.addListener(this.manager);
        this.manager.addListener(this);
        this.manager.setProxy(this);
        this.manager.setQueueable(main.getQueueable());
    }

    private void init() 
    {
        this.debugScreen = new DebugScreen(main.getJDA());  
        this.manager = new ScreenSwitchManager();
    }

    public ScreenFramework getDebugScreen() 
    {
		return this.debugScreen;
	}

	@Override
    public void onSwitch(SwitchEvent e) 
    {
        this.main.setCurrentScreen(e.getNewScreen());
        
        if (e.getNewScreen() instanceof GuildScreen )
            ((GuildScreen) e.getNewScreen()).addListener(this.manager);
        if (e.getNewScreen() instanceof PMScreen)
            ((PMScreen) e.getNewScreen()).addListener(this.manager);
        
        this.main.update();
	}

    @Override
    public ScreenFramework getDebug() 
    {
        return new DebugScreen(main.getJDA());  
    }

    @Override
    public ScreenFramework getPm() 
    {
        return new PMScreen(main.getJDA());
    }

    @Override
    public ScreenFramework getCurrentScreen() 
    {
        return main.getCurrentScreen();
    }

    
}
