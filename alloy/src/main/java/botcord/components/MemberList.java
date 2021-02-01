package botcord.components;

import botcord.components.util.BotCordPanel;
import net.dv8tion.jda.api.entities.Guild;

@SuppressWarnings("serial")
public class MemberList extends BotCordPanel {

    private Guild guild;

    public MemberList(Guild guild) 
    {
        this.guild = guild;
        init();
        config();
	}

    @Override
    public void init() 
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void config() 
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() 
    {
        // TODO Auto-generated method stub
    }

    public Guild getGuild() 
    {
        return guild;
    }
    
}
