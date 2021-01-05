package botcord.manager;

import botcord.BotCord;
import botcord.event.PressListener;
import botcord.event.ScreenSwitchEvent;
import botcord.screens.GuildScreen;
import net.dv8tion.jda.api.entities.Guild;

public class ScreenSwitchManager implements PressListener {

    private BotCord main;

    public ScreenSwitchManager(BotCord main) 
    {
        this.main = main;
    }

    @Override
    public void onSwitchScreenPress(ScreenSwitchEvent e) 
    {
        GuildScreen screen = getGuildScren(e.getGuild());
        this.main.setCurrentScreen(screen);
    }

    private GuildScreen getGuildScren(Guild guild) 
    {
        return new GuildScreen(guild, main);
	}

    
}
