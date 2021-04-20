package frontsnapk1ck.botcord.components.message.message.button.util;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.botcord.components.gui.BCButton;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.Message;

public abstract class AbstractMessageButtons extends BCButton {

    private Message message;

    public AbstractMessageButtons(Message message) 
    {
        this.message = message;
    }

    public Message getMessage() 
    {
        return message;
    }

    protected void setImage() 
    {
        try 
        {
            Image img = getImage(getImageLink());
            this.setIcon(new ImageIcon(img));
        }
        catch (IOException e) 
        {
            Alloy.LOGGER.error("GuildButton", e);
        }
    
    }

    protected abstract String getImageLink();

    private Image getImage(String urlS) throws IOException 
    {
        if (urlS == null)
            urlS = BCUtil.DEFAULT_DISCORD_PHOTO;
        URL url = new URL(urlS);
        Image img = ImageIO.read(url);
        return img;
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


}
