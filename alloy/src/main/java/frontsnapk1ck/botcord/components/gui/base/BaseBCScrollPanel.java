package frontsnapk1ck.botcord.components.gui.base;

import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class BaseBCScrollPanel extends JScrollPane {


    public BaseBCScrollPanel() 
    {
        this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        this.getVerticalScrollBar().setUnitIncrement(15);
    }


}
