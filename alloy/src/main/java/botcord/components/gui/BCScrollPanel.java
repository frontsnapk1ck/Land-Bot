package botcord.components.gui;

import botcord.components.gui.base.BaseBCScrollPanel;
import botcord.util.BCComponent;

@SuppressWarnings("serial")
public abstract class BCScrollPanel extends BaseBCScrollPanel implements BCComponent {


    public BCScrollPanel() 
    {
        this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        this.getVerticalScrollBar().setUnitIncrement(15);
    }


}
