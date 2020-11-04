package landbot.utility.timer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import landbot.utility.event.SpamFinishEvent;
import landbot.utility.event.SpamFinishListener;
import net.dv8tion.jda.api.entities.TextChannel;

public class SpamRunnable implements Runnable {
    
    private boolean stop;
    private Long id;
    private int reps;
    private String message;
    private TextChannel channel;
    private List<SpamFinishListener> listners;
    private Logger logger;


    public SpamRunnable(int reps, String message, TextChannel c) 
    {
        this.listners = new ArrayList<SpamFinishListener>();
        this.logger = LoggerFactory.getLogger(SpamRunnable.class);

        this.reps = reps;
        this.message = message;
        this.channel = c;
	}

	@Override
    public void run() 
    {
        for (int i = 0; i < this.reps; i++) 
        {
            if (this.stop)
            {
                this.alertListeners();
                return;
            }

            this.channel.sendMessage(message).queue();

            cooldown();
        }
        this.alertListeners();
    }

    private void alertListeners() 
    {
        for (SpamFinishListener l : listners) 
        {
            SpamFinishEvent e = new SpamFinishEvent();
            e.setRunnable(this);
            l.onSpamFinishEvent(e);
        }
    }

    private void cooldown() 
    {
        try 
        {
            Thread.sleep(1250);
        } 
        catch (InterruptedException e) 
        {
            logger.error("There was an error on the spam timer");
            e.printStackTrace();
        }
    }

    public void addListener( SpamFinishListener l)
    {
        this.listners.add(l);
    }

    public boolean removeListener( SpamFinishListener l)
    {
        return this.listners.remove(l);
    }

    public Long getID() 
    {
		return this.id;
	}

    public void setID(Long num) 
    {
        this.id = num;
    }

    public void stop() 
    {
        this.stop = true;
	}
}
