package frontsnapk1ck.botcord.components.gui.base;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import frontsnapk1ck.botcord.BotCord;

public class BaseBCImagePanel extends BaseBCPanel {

    private Image image;
    private boolean rescale;

    public BaseBCImagePanel(String path) 
    {
        this(new File(path), true);
    }

    public BaseBCImagePanel(String path , boolean rescale) 
    {
        this(new File(path) , rescale );
    }

    public BaseBCImagePanel(File file) 
    {
        this(file,true);
    }

    public BaseBCImagePanel(File file, boolean rescale) 
    {
        super();
        this.rescale = rescale;
        try 
        {
            image = ImageIO.read(file);
        }
        catch (IOException e) 
        {
            BotCord.LOGGER.error("BaseBCImagePanel", e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        if (this.rescale)
            rescaleImage();
        g.drawImage(image,0,0,this);
    }

    private void rescaleImage() 
    {
        int width = this.getWidth();
        int height = this.getHeight();
        image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
}
