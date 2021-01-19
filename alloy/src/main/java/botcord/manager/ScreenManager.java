package botcord.manager;

import botcord.BotCord;
import botcord.manager.util.SwitchEvent;
import botcord.manager.util.Switchable;
import botcord.screen.DebugScreen;
import gui.ScreenFramework;

public class ScreenManager implements Switchable {

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
        //TODO make main switch to the right thing
	}
    
}
