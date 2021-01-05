package botcord.manager;

import gui.ScreenFramework;

public interface ScreenInterface {

    public void setScreen(ScreenFramework screen);
    
    public ScreenSwitchManager getManager();
    
}
