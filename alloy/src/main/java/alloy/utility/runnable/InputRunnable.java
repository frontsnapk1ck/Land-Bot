package alloy.utility.runnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import input.device.DeviceListener;

public class InputRunnable implements Runnable{

    private Scanner scanner;
    private boolean running;
    private List<DeviceListener> listeners; 

    public InputRunnable() 
    {
        super();
        this.scanner = new Scanner(System.in);
        this.running = false;
        this.listeners = new ArrayList<DeviceListener>();
    }

    public boolean isRunning() 
    {
        return running;
    }

    public void stop()
    {
        this.running = false;
    }

    public void setListener(DeviceListener l) 
    {
        this.listeners.add(l);
    }
    
    public boolean removeListener( DeviceListener l )
    {
        return this.listeners.remove(l);
    }

    @Override
    public void run() 
    {
        this.running = true;
        while(scanner.hasNextLine() && this.running )
        {
            String in = scanner.nextLine();
            for (DeviceListener l : listeners) 
                l.onInput(in);
        }
    }
    
}
