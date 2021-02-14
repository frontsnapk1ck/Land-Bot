package botcord.components.message.attachment;

import java.io.File;

@SuppressWarnings("serial")
public class PhotoMessageAttachment extends AbstractMessageAttachment {

	private File photo;

    public PhotoMessageAttachment(File f) 
    {
        this.photo = f;
        init();
        config();
    }

    @Override
    public void config() 
    {
        
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
