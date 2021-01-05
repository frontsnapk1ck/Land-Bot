package botcord.manager;

import botcord.screens.DebugScreen;
import net.dv8tion.jda.api.JDA;

public class ScreenHolder {


  private DebugScreen debugScreen;


    public void config(JDA jda, ScreenInterface interf) 
    {
        this.debugScreen = new DebugScreen(jda , interf);
	}

    public DebugScreen getDebugScreen() 
    {
		return this.debugScreen;
    }

    public void update() 
    {
        // this.debugScreen.update();
    }
    
}
