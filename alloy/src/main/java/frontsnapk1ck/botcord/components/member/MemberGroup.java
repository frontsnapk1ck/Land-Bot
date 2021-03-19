package frontsnapk1ck.botcord.components.member;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import frontsnapk1ck.botcord.components.gui.BCPanel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

@SuppressWarnings("serial")
public class MemberGroup extends BCPanel {

    public static final int COMP_HEIGHT = 30;

    private List<Member> members;
    private List<MemberButton> buttons;
    private JLabel label;
    private Role role;
    private String name;

    public MemberGroup(List<Member> members , Role r ) 
    {
        super();
        this.members = members;
        this.role = r;
        init();
        config();
    }

    @Override
    public void init() 
    {
        this.buttons = new ArrayList<MemberButton>();
        this.label = new JLabel();
        this.name = role.getName() + "--" + members.size();
        this.setForeground(role.getColor());
        for (Member m : members)
        {
            MemberButton b = new MemberButton( m );
            b.setForeground(role.getColor());
            this.buttons.add(b);
        }
    }

    @Override
    public void config() 
    {
        this.setBackground(null);
        for (MemberButton b : buttons) 
            this.add(b);
        this.configLabel();
        this.configToolTip();
    }

    private void configToolTip() 
    {
        this.setToolTipText(this.name);
    }

    private void configLabel() 
    {
        this.label.setText(this.name);
        this.label.setForeground(this.role.getColor());
    }

    @Override
    public void update() 
    {
    }

    public JLabel getLabel() {
        return label;
    }

    public List<MemberButton> getButtons() {
        return buttons;
    }

	public int getNumComps() 
    {
		return this.buttons.size() + 1;
	}
    
}
