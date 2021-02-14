package botcord.components.message.attachment;

import net.dv8tion.jda.api.entities.MessageEmbed;

@SuppressWarnings("serial")
public class EmbedMessageAttachment extends AbstractMessageAttachment {

	private MessageEmbed embed;

    public EmbedMessageAttachment(MessageEmbed embed) 
    {
        this.embed = embed;
        init();
        config();
	}

    @Override
    public void config() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBounds() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }
    
}
