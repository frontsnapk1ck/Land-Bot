package botcord.manager.util;

import gui.ScreenFramework;

public interface ScreenProxy {

    public ScreenFramework getDebug();

    public ScreenFramework getPm();
    
    public ScreenFramework getCurrentScreen();
}
