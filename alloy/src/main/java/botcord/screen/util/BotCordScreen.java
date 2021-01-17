package botcord.screen.util;

import botcord.util.BotCordComponent;
import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class BotCordScreen extends Scene implements BotCordComponent {

    public BotCordScreen(Parent root, double width, double height) 
    {
        super(root, width, height);
    }
    
}
