package frontsnapk1ck.botcord.components.gui;

import frontsnapk1ck.botcord.components.gui.base.BaseBCScrollPanel;
import frontsnapk1ck.botcord.util.BCComponent;

@SuppressWarnings("serial")
public abstract class BCScrollPanel extends BaseBCScrollPanel implements BCComponent {


    public BCScrollPanel() 
    {
        this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        this.getVerticalScrollBar().setUnitIncrement(15);
    }


}
