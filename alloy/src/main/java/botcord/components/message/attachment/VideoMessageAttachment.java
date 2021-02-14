package botcord.components.message.attachment;

import java.io.File;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

@SuppressWarnings("serial")
public class VideoMessageAttachment extends AbstractMessageAttachment {

	private File video;
    private EmbeddedMediaPlayerComponent player;

    public VideoMessageAttachment(File f) 
    {
        this.video = f;
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
