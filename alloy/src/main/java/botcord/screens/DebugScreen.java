package botcord.screens;

import java.util.ArrayList;
import java.util.List;

import alloy.event.DebugEvent;
import botcord.componets.DebugLog;
import botcord.event.DebugListener;
import botcord.event.PressListener;
import botcord.manager.ScreenInterface;
import botcord.screens.util.AbsrtactScreen;
import net.dv8tion.jda.api.JDA;
import utility.StringUtil;
import utility.time.TimeUtil;

public class DebugScreen extends AbsrtactScreen implements DebugListener {

    private List<DebugEvent> events;
    private DebugLog debugOutput;
    private JDA jda;

    public DebugScreen(JDA jda, ScreenInterface interf) {
        super();
        this.events = new ArrayList<DebugEvent>();
        this.jda = jda;
        configJLable();
        setManager(interf.getManager());
        configSelector();
        configPannel();
    }

    private void configSelector() 
    {
        List<PressListener> listeners = new ArrayList<PressListener>();
        listeners.add(this.getManager());
        this.configSelector(listeners, this.jda);
    }

    @Override
    protected void configPannel() 
    {
        this.getPanel().setLayout(null);
        this.getPanel().add(this.debugOutput);
        this.getPanel().add(this.getSelector());
    }

    private void configJLable() {
        this.debugOutput = new DebugLog();
    }

    @Override
    public void onRecieve(DebugEvent e) {
        this.events.add(e);
        this.updateList();
    }

    protected void updateList() {
        String[][] table = new String[this.events.size()][5];

        int i = 0;
        for (DebugEvent e : events) {
            table[i][0] = TimeUtil.getMidTime(e.getTime());
            table[i][1] = e.getThread().getName();
            table[i][2] = e.getClassname();
            table[i][3] = e.getLevel().toString();
            if (e.hasMessage())
                table[i][4] = e.getMessage();
            else if (e.hasError())
                table[i][4] = e.getError().getMessage();
            if (table[i][4] == null)
                ;
            table[i][4] = "";

            i++;
        }

        String out = StringUtil.makeTable(table);
        this.debugOutput.setText(out);
    }

    @Override
    protected void onScreenResize() {
        int x = (int) (this.getWidth() * (1f - SCREEN_WIDTH));
        int w = (int) (this.getWidth() * SCREEN_WIDTH);
        int y = (int) (this.getHeight() * (1f - SCREEN_HEIGHT));
        int h = (int) (this.getHeight() * SCREEN_HEIGHT);

        this.debugOutput.setBounds(x, y, w, h);
        this.getSelector().setBounds(0, 0, x, h);

        this.update();
    }

    @Override
    public void update() {
        this.getSelector().update();
    }

}
