package botcord.components.message.message.attachment;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

@SuppressWarnings("serial")
public class VideoMessageAttachment extends AbstractMessageAttachment {

	private String link;
    private EmbeddedMediaPlayerComponent player;

    public VideoMessageAttachment(String string) 
    {
        this.link = string;
        init();
        config();
	}

    @Override
    public void config() 
    {
        updateBounds();
    }

    @Override
    public void update() 
    {
        updateBounds();
    }

    @Override
    public void updateBounds() 
    {
        this.player.setBounds(this.getBounds());
    }

    @Override
    public void init() 
    {
        player = new EmbeddedMediaPlayerComponent();
    }
    
}
