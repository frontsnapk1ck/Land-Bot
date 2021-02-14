package botcord.components.gui;

import botcord.components.gui.base.BaseBCPanel;
import botcord.util.BCComponent;

@SuppressWarnings("serial")
public abstract class BCPanel extends BaseBCPanel implements BCComponent {

    public BCPanel() 
    {
        super();
        this.setLayout(null);
    }
    
}
