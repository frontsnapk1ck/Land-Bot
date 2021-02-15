package botcord;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import alloy.event.DebugEvent;
import alloy.event.DebugListener;
import alloy.main.Alloy;
import botcord.manager.ScreenManager;
import botcord.util.BCUtil;
import botcord.util.event.BotCordLogger;
import gui.WindowFramework;
import net.dv8tion.jda.api.JDA;

@SuppressWarnings("serial")
public class BotCord extends WindowFramework{

    public static final BotCordLogger LOGGER = new BotCordLogger();

    private Alloy alloy;
    private ScreenManager manager;


    public BotCord(Alloy alloy) 
    {
        this.alloy = alloy;
        init();
    }

    private void config() 
    {
        configCache();
        configScreens();
    }
    
    private void configCache() 
    {
        BCUtil.loadCache(alloy);
    }

    private void configScreens() 
    {
        this.manager = new ScreenManager(this);
        this.setCurrentScreen(this.manager.getDebugScreen());
        this.update();
    }

    private void init() 
    {
        this.setTitle("BotCord Testing");
        this.setSize(1600, 900);
        this.setIconImage(loadIcon());
        this.setVisible(true);
    }

    private Image loadIcon() 
    {
        try 
        {
            URL url = new URL(BCUtil.APP_ICON);
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
		return this.alloy.getJDA();
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

	public void finishInit() 
    {
        config();
        update();
	}

}
