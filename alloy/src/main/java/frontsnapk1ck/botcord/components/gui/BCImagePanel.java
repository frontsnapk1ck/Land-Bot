package frontsnapk1ck.botcord.components.gui;

import java.io.File;

import frontsnapk1ck.botcord.components.gui.base.BaseBCImagePanel;
import frontsnapk1ck.botcord.util.BCComponent;

@SuppressWarnings("serial")
public abstract class BCImagePanel extends BaseBCImagePanel implements BCComponent {

    public BCImagePanel(String path) 
    {
        this(new File(path), true);
    }

    public BCImagePanel(String path , boolean rescale) 
    {
        this(new File(path) , rescale );
    }

    public BCImagePanel(File file) 
    {
        this(file,true);
    }

    public BCImagePanel(File file, boolean rescale)
    {
        super(file, rescale);
    }
    
}
