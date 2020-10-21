package landbot.utility;

import net.dv8tion.jda.api.entities.TextChannel;

public class SpamRunnable implements Runnable {
    
    private boolean stop;
    private Long id;
    private int reps;
    private String message;
    private TextChannel channel;


    public SpamRunnable(int reps, String message, TextChannel c) 
    {
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
                return;

            this.channel.sendMessage(message).queue();

            cooldown();
        }
    }

    private void cooldown() 
    {
        try 
        {
            Thread.sleep(1000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
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
