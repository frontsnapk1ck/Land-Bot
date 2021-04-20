package frontsnapk1ck.botcord.components.gui.base;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;

import frontsnapk1ck.botcord.event.BCListener;

public abstract class BaseBCButton extends JButton  {

    private List<BCListener> listeners;

    public BaseBCButton() 
    {
        super();
        this.configListeners();
        this.listeners = new ArrayList<BCListener>();
    }

    protected void configListeners()
    {
        this.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                    onRightClick(getModifiers(e));
                if (e.getButton() == MouseEvent.BUTTON3)
                    onLeftClick(getModifiers(e));
            }
        });
    }

    protected void onLeftClick(Set<MouseModifiers> modifiers) 
    {
        //nothing to do yet
    }

    protected void onRightClick(Set<MouseModifiers> modifiers)
    {
        //nothing to do yet
    }

    protected Set<MouseModifiers> getModifiers(MouseEvent e) 
    {
        List<MouseModifiers> mods = new ArrayList<MouseModifiers>();
        if (e.isMetaDown())
            mods.add(MouseModifiers.META);
        if (e.isShiftDown())
            mods.add(MouseModifiers.SHIFT);
        if (e.isAltDown())
            mods.add(MouseModifiers.ALT);
        if (e.isControlDown())
            mods.add(MouseModifiers.CONTROL);
        Set<MouseModifiers> modSet = new HashSet<MouseModifiers>(mods);
        return modSet;
    }

    public void setListeners(List<BCListener> listeners) 
    {
        this.listeners = listeners;
    }

    public List<BCListener> getListeners() {
        return listeners;
    }

    public void addListener(BCListener l)
    {
        this.listeners.add(l);
    }
    
    public boolean rmListener(BCListener l)
    {
        return this.listeners.remove(l);
    }

    protected enum MouseModifiers{
        ALT,
        SHIFT,
        META,
        CONTROL,
    }
}
