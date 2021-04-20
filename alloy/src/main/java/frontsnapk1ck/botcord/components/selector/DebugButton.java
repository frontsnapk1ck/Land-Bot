package frontsnapk1ck.botcord.components.selector;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.botcord.components.gui.BCButton;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.event.PressEvent;
import frontsnapk1ck.botcord.event.SwitchTarget;
import frontsnapk1ck.botcord.util.BCUtil;

public class DebugButton extends BCButton {

    public DebugButton() {
        super();
        init();
        config();
    }

    @Override
    public void init() 
    {
        this.setBackground(null);
        this.setBorder(null);
    }

    @Override
    public void config() 
    {
        this.configTooltip();
        setImage();
    }

    private void setImage() 
    {
        try 
        {
            URL url = new URL(BCUtil.BUG_IMAGE);
            Image img = ImageIO.read(url);
            this.setIcon(new ImageIcon(img));
        }
        catch (IOException e) 
        {
            Alloy.LOGGER.warn("DebugButton", e.getLocalizedMessage());
        }
        
    }

    public void updateImage() 
    {
        int w, h;
        w = this.getWidth();    
        h = this.getHeight();

        if (w == 0 || h == 0)
            return;
        
        Icon ic = this.getIcon();
        if (ic instanceof ImageIcon) 
        {
            Image img = ((ImageIcon) ic).getImage();
            img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            ImageIcon newIc = new ImageIcon(img);
            this.setIcon(newIc);
        }
    }

    private void configTooltip() 
    {
        this.setToolTipText("Debug Screen");
    }

    @Override
    public void update() 
    {
        updateImage();
    }

    @Override
    protected void onRightClick(Set<MouseModifiers> modifiers) 
    {
        PressEvent e = new PressEvent(SwitchTarget.DEBUG);
        for (BCListener l : getListeners())
            l.onPress(e);
    }
    
}
