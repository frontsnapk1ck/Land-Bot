package frontsnapk1ck.botcord.components.message.message.button;

import java.util.Set;

import frontsnapk1ck.botcord.BotCord;
import frontsnapk1ck.botcord.components.message.message.button.util.AbstractMessageButtons;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.Message;

@SuppressWarnings("serial")
public class MessageDeleteButton extends AbstractMessageButtons {

    public MessageDeleteButton(Message message) 
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
        try 
        {
            getMessage().delete().complete();
        }
        catch (Exception e) 
        {
            BotCord.LOGGER.warn("MessageDeleteButton", "i couldn't delete the message\t" + getMessage() );
        }
    }

    @Override
    protected String getImageLink() 
    {
        return BCUtil.DELETE_ICON;
    }
    
}
