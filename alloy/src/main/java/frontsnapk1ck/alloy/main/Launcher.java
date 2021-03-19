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

    public static void main(String[] args) {
        Launcher l = new Launcher();
        l.launchAll(args);
    }

    public Launcher() {
        System.out.println("Launched Alloy with version " + Launcher.VERSION);
    }

    private void launchAll(String[] args) {
        try {
            alloy = new Alloy();
            botCord = new BotCord(alloy);
            updateActivityRunnable();
        } catch (Exception e) {
            Alloy.LOGGER.error("Launcher", e);
            return;
        }
    }

    private static void updateActivityRunnable() 
    {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                alloy.finishInit();
                botCord.finishInit();
                botCord.addLoggerListener(alloy.getInterfaceListener());
            }
        };

        Thread t = new Thread(r, "Update Delay");
        t.setDaemon(true);
        t.start();
    }

}
