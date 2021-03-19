package frontsnapk1ck.botcord.manager.util;

import frontsnapk1ck.gui.ScreenFramework;

public class SwitchEvent {

	private ScreenFramework newScreen;

    public SwitchEvent(ScreenFramework newScreen) 
    {
        this.newScreen = newScreen;
	}
	
	public ScreenFramework getNewScreen() 
	{
		return newScreen;
	}

    
}
