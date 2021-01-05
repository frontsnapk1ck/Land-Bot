package botcord.componets.selector;

import java.util.List;

import javax.swing.JPanel;

import botcord.event.PressListener;
import botcord.util.GuiUtil;
import net.dv8tion.jda.api.JDA;

@SuppressWarnings("serial")
public class ScreenSelector extends JPanel {

    public static final float   SPLIT = 0.80f;

    private GuildSelector guildSelector;
    private AlloySelector alloySelector;


    public ScreenSelector(JDA jda) 
    {
        System.out.println("\n\n\t\tScreenSelector.ScreenSelector()\n\n");
        this.guildSelector = new GuildSelector(jda);
        this.alloySelector = new AlloySelector();
        setBounds();
        config();
        
        this.setToolTipText("Screen Selector");
        this.setBackground(GuiUtil.randomColor());
	}

    private void config() 
    {
        this.add(this.guildSelector);
        this.add(this.alloySelector);
    }

    private void setBounds() 
    {
        System.out.println("\n\tScreenSelector.setBounds()\n");
        int h = (int)(this.getHeight() * SPLIT);
        this.guildSelector.setBounds(0,0,this.getWidth(),h);
        this.alloySelector.setBounds(0,h,this.getWidth(),this.getHeight()-h);
    }

    public void setActionListeners(List<PressListener> ls) 
    {
        this.guildSelector.setActionListeners(ls);
        this.alloySelector.setActionListeners(ls);
	}

    public void update() 
    {
        setBounds();
        this.alloySelector.update();
        this.guildSelector.update();
	}

   

}
