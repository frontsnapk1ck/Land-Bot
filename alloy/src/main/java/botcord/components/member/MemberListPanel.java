package botcord.components.member;

import botcord.components.gui.BotCordPanel;
import net.dv8tion.jda.api.entities.Guild;

@SuppressWarnings("serial")
public class MemberListPanel extends BotCordPanel {

    private Guild guild;
    private int maxH;

    public MemberListPanel(Guild guild) 
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

    public int getMaxH() 
    {
		return this.maxH;
	}
    
}
