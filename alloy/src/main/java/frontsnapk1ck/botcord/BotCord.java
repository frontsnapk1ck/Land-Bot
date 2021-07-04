package frontsnapk1ck.botcord;

import java.awt.Image;
import java.awt.GraphicsEnvironment;

import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import frontsnapk1ck.alloy.event.DebugEvent;
import frontsnapk1ck.alloy.event.DebugListener;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.main.intefs.Queueable;
import frontsnapk1ck.botcord.manager.ScreenManager;
import frontsnapk1ck.botcord.util.BCUtil;
import frontsnapk1ck.botcord.util.event.BotCordLogger;
import frontsnapk1ck.gui.WindowFramework;
import net.dv8tion.jda.api.JDA;

public class BotCord extends WindowFramework {

    public static final BotCordLogger LOGGER = new BotCordLogger();
    public static final boolean IS_ENABLED;

    private Alloy alloy;
    private ScreenManager manager;

    static {
        IS_ENABLED = findIsEnabled();
    }


    public BotCord(Alloy alloy) 
    {
        this.alloy = alloy;
        System.err.println(IS_ENABLED);
        if (IS_ENABLED)
        {
            System.out.println("BotCord.BotCord()");
            try
            {
                init();
            }
            catch (Exception e){
            }
        }
        else
            LOGGER.info("BotCord", "No Display detected, GUI not initializing");
    }

    private static boolean findIsEnabled()
    {
        return !GraphicsEnvironment.isHeadless();
    }

    private void config() 
    {
        configCache();
        configEventQueue();
        configScreens();
    }
    
    private void configEventQueue() 
    {
        BCUtil.loadQueue(alloy);
    }

    private void configCache() 
    {
        BCUtil.loadCache();
    }

    private void configScreens() 
    {
        this.manager = new ScreenManager(this);
        this.setCurrentScreen(this.manager.getDebugScreen());
        this.update();
    }

    private void init() 
    {
        this.setTitle("BotCord");
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
        if (!IS_ENABLED)
        {
            LOGGER.info("BotCord", "I caught what would've been an error. you're welcome");
            return;
        }
        config();
        update();
	}

    public Queueable getQueueable() 
    {
        return alloy;
    }

}
