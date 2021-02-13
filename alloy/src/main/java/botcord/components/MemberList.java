package botcord.components;

import botcord.components.util.BotCordScrollPanel;
import botcord.util.BotCordUtil;
import net.dv8tion.jda.api.entities.Guild;

@SuppressWarnings("serial")
public class MemberList extends BotCordScrollPanel {

    private MemberListPanel panel;

    public MemberList(Guild guild) 
    {
        this.panel = new MemberListPanel(guild);
        init();
        config();
    }
    
    @Override
    public void init() 
    {
        this.setBackground(BotCordUtil.CHANNEL_SELECTOR);
    }

    @Override
    public void config() 
    {
        this.setViewportView(this.panel);
    }

    @Override
    public void update() 
    {
        panel.setBounds(this.getBounds());
        panel.update();
        panel.setBounds(0,0,panel.getWidth(),panel.getMaxH());
    }
}
