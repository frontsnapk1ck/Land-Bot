package botcord.components.gui;

import javax.swing.JScrollPane;

import botcord.util.BotCordComponent;

@SuppressWarnings("serial")
public abstract class BotCordScrollPanel extends JScrollPane implements BotCordComponent {


    public BotCordScrollPanel() 
    {
        this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        this.getVerticalScrollBar().setUnitIncrement(15);
    }


}
