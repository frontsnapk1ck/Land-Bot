package botcord.components.message;

import botcord.components.gui.BCButton;
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
        this.setText(this.member.getEffectiveName());
        this.setToolTipText(this.member.getEffectiveName());
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
        configListener();
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
    protected void configListener() 
    {
        if (isMember())
        configListenerMember();
    else
        configListenerUser();
    }

    private void configListenerMember() 
    {
        //TODO popup menu coming soon
    }

    private void configListenerUser() 
    {
        //TODO popup menu coming soon
    }

    private enum Type {
        MEMBER,
        USER,
    }

}
