package botcord.components.gui;

import javax.swing.JPanel;

import botcord.util.BotCordComponent;

@SuppressWarnings("serial")
public abstract class BotCordPanel extends JPanel implements BotCordComponent {

    public BotCordPanel() 
    {
        super();
        this.setLayout(null);
    }
    
}
