package botcord.components.button;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import alloy.main.Alloy;
import botcord.components.util.BotCordButton;
import botcord.event.BotCordListener;
import botcord.event.PressEvent;
import botcord.event.PressTarget;
import botcord.util.BotCordLinks;

@SuppressWarnings("serial")
public class DebugButton extends BotCordButton {

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
    public void config() {
        this.configTooltip();
        configListener();
        setImage();
    }

    private void setImage() {
        URL url;
        try {
            url = new URL(BotCordLinks.BUG_IMAGE);
            Image img = ImageIO.read(url);
            this.setIcon(new ImageIcon(img));
        } catch (IOException e) 
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
    protected void configListener() 
    {
        this.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ignored) 
            {
                PressEvent e = new PressEvent(PressTarget.DEBUG);
                for (BotCordListener l : getListeners())
                    l.onPress(e);
            }
        });
    }
    
}
