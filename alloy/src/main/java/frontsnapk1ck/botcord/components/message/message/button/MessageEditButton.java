package frontsnapk1ck.botcord.components.message.message.button;

import java.util.Set;

import frontsnapk1ck.botcord.components.message.message.button.util.AbstractMessageButtons;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.Message;

@SuppressWarnings("serial")
public class MessageEditButton extends AbstractMessageButtons {

    public MessageEditButton(Message message) 
    {
        super(message);
        init();
        config();
	}

	@Override
    public void init() 
    {
    }

    @Override
    public void config() 
    {
        setImage();
    }

    @Override
    public void update() 
    {
        updateImage();
    }
    
    @Override
    protected void onRightClick(Set<MouseModifiers> modifiers) 
    {
        System.err.println("so you want to edit?");
        //coming soon
    }

    @Override
    protected String getImageLink() 
    {
        return BCUtil.EDIT_ICON;
    }
    
}
