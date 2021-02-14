package botcord.components.message.button;

import botcord.components.message.button.util.AbstractMessageButtons;
import botcord.util.BotCordUtil;
import net.dv8tion.jda.api.entities.Message;

@SuppressWarnings("serial")
public class MessageDeleteButton extends AbstractMessageButtons {

    public MessageDeleteButton(Message message) 
    {
        super(message);
	}

	@Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void config() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() 
    {
        updateImage();
    }

    @Override
    protected void configListener() {
        // TODO Auto-generated method stub

    }

    @Override
    protected String getImageLink() 
    {
        return BotCordUtil.DELETE_ICON;
    }
    
}
