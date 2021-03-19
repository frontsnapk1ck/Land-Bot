package frontsnapk1ck.botcord.components.message.message.button;

import java.util.Set;

import frontsnapk1ck.botcord.components.gui.BCButton;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

@SuppressWarnings("serial")
public class UserButton extends BCButton {

    private User user;
    private Type type;
    private Member member;

    public UserButton(User u) 
    {
        super();
        this.user = u;
        this.type = Type.USER;
        init();
        config();
    }

    public UserButton(Member m) 
    {
        super();
        this.member = m;
        this.type = Type.MEMBER;
        init();
        config();
    }

    @Override
    public void init() 
    {
        this.setBackground(null);
		this.setBorder(null);
    }

    private void configUser() 
    {
        this.setText(this.user.getName());
        this.setToolTipText(this.user.getName());
    }

    private void configMember() 
    {
        try 
        {
            this.setText(this.member.getEffectiveName());
            this.setToolTipText(this.member.getEffectiveName());
        }
        catch (Exception e) 
        {
            System.err.println("mama mia");
        }
    }

    public boolean isMember() 
    {
        return this.type == Type.MEMBER;
    }

    @Override
    public void config() 
    {
        if (isMember())
            configMember();
        else
            configUser();
    }

    @Override
    public void update() 
    {
        if (isMember())
            configMember();
        else
            configUser();
    }

    @Override
    protected void onRightClick(Set<MouseModifiers> modifiers) 
    {
        if (this.isMember())
            memberRClick(modifiers);
        else 
            userRClick(modifiers);
    }

    @Override
    protected void onLeftClick(Set<MouseModifiers> modifiers) 
    {
        if (this.isMember())
            memberLClick(modifiers);
        else 
            userLClick(modifiers);
    }

    private void memberLClick(Set<MouseModifiers> modifiers) 
    {
        //coming soon
    }

    private void userLClick(Set<MouseModifiers> modifiers) 
    {
        //coming soon
    }

    private void memberRClick(Set<MouseModifiers> modifiers) 
    {
        //coming soon
    }

    private void userRClick(Set<MouseModifiers> modifiers) 
    {
        //coming soon
    }

    private enum Type {
        MEMBER,
        USER,
    }

}
