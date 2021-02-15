package botcord.components.message.message;

import java.awt.GridLayout;

import botcord.components.gui.BCPanel;
import botcord.components.message.message.button.UserButton;
import botcord.components.message.message.button.util.DisMessageButtons;

@SuppressWarnings("serial")
public class MessageTopRow extends BCPanel {

    private UserButton user;
    private DisMessageButtons buttons;

    public MessageTopRow(DisMessageButtons buttons, UserButton button) 
    {
        this.buttons = buttons;
        this.user = button;
    }

    @Override
    public void init() 
    {
        this.setLayout( new GridLayout(1,2));
    }

    @Override
    public void config() 
    {
        this.add(this.user);
        this.add(this.buttons);
    }

    @Override
    public void update() 
    {
        this.user.update();
        this.buttons.update();
    }
    
}
