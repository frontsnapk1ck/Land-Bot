package botcord.manager.util;

import gui.ScreenFramework;

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
