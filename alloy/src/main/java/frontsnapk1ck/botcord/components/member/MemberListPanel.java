package frontsnapk1ck.botcord.components.member;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.botcord.components.gui.BCPanel;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class MemberListPanel extends BCPanel {

    private Guild guild;
    private int maxH;

    private List<MemberGroup> groups;
    private List<BCListener> listeners;

    public MemberListPanel(Guild guild) 
    {
        this.guild = guild;
        init();
        config();
	}

    @Override
    public void init() 
    {
        this.groups = new ArrayList<MemberGroup>();
        this.setBackground(BCUtil.CHANNEL_SELECTOR);
    }

    @Override
    public void config() 
    {
        this.configToolTip();
        this.makeGroups();
        this.configGroups();
        this.updateBounds();
        this.updateLayout();
    }

    private void updateLayout() 
    {
        int comps = 0;
        for (MemberGroup cg : groups) 
            comps += cg.getNumComps();
        this.setLayout(new GridLayout(comps,1,0,10));
    }

    private void updateBounds() 
    {
        int w = this.getWidth();
        for (MemberGroup cg : groups) 
        {
            int h = cg.getNumComps() * MemberGroup.COMP_HEIGHT;
            cg.setSize( w, h );
        }
    }

    private void configGroups() 
    {
        for (MemberGroup memberGroup : groups)
        {
            this.add(memberGroup.getLabel());
            for (MemberButton b : memberGroup.getButtons())
                this.add(b);
        }
    }

    private void makeGroups() 
    {
        List<Member> shown = new ArrayList<Member>();
        List<Role> roles = this.guild.getRoles();
        for (Role role : roles) 
        {
            if (role.isHoisted())
            {
                List<Member> members = guild.getMembersWithRoles(role);
                members.removeAll(shown);
                MemberGroup group = new MemberGroup(members, role);
                group.setForeground(role.getColor());
                shown.addAll(members);
                this.groups.add(group);
            }
        }
    }

    private void configToolTip() 
    {
        this.setToolTipText("Member List");
    }

    @Override
    public void update() 
    {
        updateBounds();
        for (MemberGroup memberGroup : groups) 
            memberGroup.update();
    }

    public Guild getGuild() 
    {
        return guild;
    }

    public int getMaxH() 
    {
		return this.maxH;
	}

    public List<BCListener> getListeners() 
    {
        return listeners;
    }
    
}
