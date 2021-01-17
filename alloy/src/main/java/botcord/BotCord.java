package botcord;

import alloy.event.DebugEvent;
import alloy.event.DebugListener;
import botcord.screen.DebugScreen;
import botcord.screen.util.BotCordScreen;
import botcord.util.BotCordColors;
import botcord.util.BotCordLinks;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.dv8tion.jda.api.JDA;

public class BotCord implements BotCordColors, BotCordLinks {


    private JDA jda;
    private BotCordScreen currentScreen;

    public BotCord()
    {
    }

    public BotCord(JDA jda) 
    {
        this.jda = jda;
        configScreens();
    }

    private void configScreens() 
    {

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
            public void onReceive(DebugEvent e) {
                System.out.println("BotCord.getDebugListener() new DebugListener() {...}.onRecieve()");
            }
        };
	}

    public void start(Stage primaryStage) throws Exception 
    {
        config(primaryStage);
        this.currentScreen = new DebugScreen( 1600, 900 );
        primaryStage.setScene(this.currentScreen); 
        primaryStage.show();
	}

    private void config(Stage primaryStage) 
    {
        primaryStage.setTitle("BotCord");
        primaryStage.setOnCloseRequest( new EventHandler<WindowEvent>()
        {
            public void handle(WindowEvent event) 
            {
                Platform.exit();
                System.exit(0);
            }
        });
    }

}
