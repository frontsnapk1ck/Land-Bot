package frontsnapk1ck.alloy.main;

import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.botcord.BotCord;

public class Launcher {

    public static final String VERSION;

    private static Alloy alloy;
    private static BotCord botCord;

    static {
        VERSION = findVersion();
    }

    private static String findVersion() 
    {
        return AlloyUtil.getVersion();
    }

    public static void main(String[] args) 
    {
        Launcher l = new Launcher();
        l.launchAll(args);
        l.config();
    }

    private void config() 
    {
        setLoggerInterceptor();
        updateActivityRunnable();
    }

    public Launcher() 
    {
        System.out.println("Launching Alloy with version " + Launcher.VERSION);
    }

    private void launchAll(String[] args) 
    {
        try 
        {
            alloy = new Alloy();
            botCord = new BotCord(alloy);
        } catch (Exception e) {
            Alloy.LOGGER.error("Launcher", e);
            return;
        }
    }

    private static void setLoggerInterceptor()
    {
        //todo
    }

    private static void updateActivityRunnable() 
    {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                alloy.finishInit();
                try
                {
                    botCord.finishInit();
                    botCord.addLoggerListener(alloy.getInterfaceListener());
                } catch (Exception e) {
                    Alloy.LOGGER.error("Launcher", e);
                }
            }
        };

        Thread t = new Thread(r, "Update Delay");
        t.setDaemon(true);
        t.start();
    }

}
