package botcord;

import alloy.event.DebugEvent;
import alloy.event.DebugListener;
import botcord.util.BotCordColors;
import botcord.util.BotCordLinks;
import gui.WindowFramework;
import net.dv8tion.jda.api.JDA;

public class BotCord extends WindowFramework implements BotCordColors, BotCordLinks {

    private static final long serialVersionUID = 2438097905045342324L;

    private JDA jda;

    public BotCord(JDA jda) {
        this.jda = jda;
        configScreens();
        init();
    }

    private void configScreens() 
    {

    }

    private void init() 
    {
        this.setSize(1600, 900);
        this.setVisible(true);
    }

    public void update() {
        this.revalidate();
    }

    public JDA getJDA() 
    {
		return this.jda;
	}

	public DebugListener getDebugListener() {
		return new DebugListener(){
            @Override
            public void onRecieve(DebugEvent e) {
                System.out.println("BotCord.getDebugListener().new DebugListener() {...}.onRecieve()");
            }
        };
	}

}
