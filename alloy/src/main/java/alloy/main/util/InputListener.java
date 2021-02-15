package alloy.main.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import alloy.utility.event.ConsoleInputListener;

public class InputListener implements Runnable {

    private List<ConsoleInputListener> listeners;
    private boolean running;

    public InputListener() 
    {
        super();
        this.listeners = new ArrayList<>();
        this.running = true;
    }

    @Override
    public void run() 
    {
        Scanner s = new Scanner(System.in);
        while (running)
        {
            alertListeners(s.nextLine());
        }
        s.close();

    }

    private void alertListeners(String input) 
    {
        for (ConsoleInputListener l : listeners)
            l.onConsoleInput(input);
    }

    public void addListener(ConsoleInputListener l) 
    {
        this.listeners.add(l);
	}

    public boolean removeListener( ConsoleInputListener l)
    {
        return this.listeners.remove(l);
    }

    
}
