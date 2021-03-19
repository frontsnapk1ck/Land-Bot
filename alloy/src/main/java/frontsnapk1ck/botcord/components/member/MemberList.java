package frontsnapk1ck.botcord.components.member;

import java.util.List;

import frontsnapk1ck.botcord.components.gui.BCScrollPanel;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.Guild;

@SuppressWarnings("serial")
public class MemberList extends BCScrollPanel {

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
        this.setBackground(BCUtil.CHANNEL_SELECTOR);
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
    }

	public void updateListeners(List<BCListener> listeners) {
	}
}
