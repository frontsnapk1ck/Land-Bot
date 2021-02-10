package botcord;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import alloy.event.DebugEvent;
import alloy.event.DebugListener;
import botcord.manager.ScreenManager;
import botcord.util.BotCordColors;
import botcord.util.BotCordLinks;
import botcord.util.event.BotCordLogger;
import gui.WindowFramework;
import net.dv8tion.jda.api.JDA;

@SuppressWarnings("serial")
public class BotCord extends WindowFramework implements BotCordColors, BotCordLinks {

    public static final BotCordLogger LOGGER = new BotCordLogger();

    private JDA jda;
    private ScreenManager manager;

    public BotCord(JDA jda) {
        this.jda = jda;
        configScreens();
        init();
    }

    private void configScreens() {
        this.manager = new ScreenManager(this);
        this.setCurrentScreen(this.manager.getDebugScreen());
        this.update();
    }

    private void init() {
        this.setTitle("BotCord Testing");
        this.setSize(1600, 900);
        this.setIconImage(loadIcon());
        this.setVisible(true);
    }

    private Image loadIcon() 
    {
        try 
        {
            URL url = new URL(APP_ICON);
            return ImageIO.read(url);
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return null;
    }

    public void update() 
    {
        this.revalidate();
        this.repaint();
        this.setSize(this.getWidth(), this.getHeight() + 1);
        this.setSize(this.getWidth(), this.getHeight() - 1);
    }

    public JDA getJDA() 
    {
		return this.jda;
	}

    public DebugListener getDebugListener() 
    {
        return new DebugListener()
        {
			@Override
            public void onReceive(DebugEvent e) 
            {
                System.out.println("BotCord.getDebugListener().new DebugListener() {...}.onRecieve()");	
			}
        };
	}

    public void addLoggerListener(DebugListener interfaceListener) 
    {
        LOGGER.addListener(interfaceListener);
	}

}
