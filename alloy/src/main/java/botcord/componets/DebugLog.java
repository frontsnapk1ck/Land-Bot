package botcord.componets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import botcord.BotCord;

@SuppressWarnings("serial")
public class DebugLog extends JPanel {
    
    private JLabel lablel;

    public DebugLog() 
    {
        super();
        configLable();
    }

    private void configLable() 
    {
        this.lablel = new JLabel();
        this.lablel.setBackground(BotCord.BACKGROUND);
        this.lablel.setForeground(BotCord.TEXT);
    }

	public void setText(String out) {
	}

}
