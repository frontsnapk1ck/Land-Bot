package botcord.componets.selector;

import java.util.List;

import javax.swing.JPanel;

import botcord.event.PressListener;

@SuppressWarnings("serial")
public class AlloySelector extends JPanel {

    private List<PressListener> listeners;

    public void setActionListeners(List<PressListener> ls) 
    {
        this.listeners = ls;
	}

	public void update() {
	}
    
}
