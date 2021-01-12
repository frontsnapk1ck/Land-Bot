package alloy.main;

import botcord.BotCord;

public class Launcher {

    public static final String VERSION;

    static {
        VERSION = findVersion();
    }

    private static Alloy alloy;
    private static BotCord botCord;

    public static void main(String[] args) {
        Launcher.init();
        try {
            alloy = new Alloy();
            botCord = new BotCord(alloy.getJDA());
            alloy.setDebugListener(botCord.getDebugListener());
        } catch (Exception e) {
            Alloy.LOGGER.error("Launcher", e);
        }

        updateActivityRunnable();
    }

    private static String findVersion() {
        return "0.2.0";
    }

    private static void init() {
        System.out.println("Launched Alloy with version " + Launcher.VERSION);
    }

    private static void updateActivityRunnable() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                alloy.update();
                botCord.update();
            }
        };

        Thread t = new Thread(r, "stupid wait for the server to update");
        t.setDaemon(true);
        t.start();
    }

}
