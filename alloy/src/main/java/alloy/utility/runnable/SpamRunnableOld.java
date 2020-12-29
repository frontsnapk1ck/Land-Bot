package alloy.utility.runnable;

import alloy.utility.job.SpamRunnable;
import net.dv8tion.jda.api.entities.TextChannel;

public class SpamRunnableOld extends SpamRunnable implements Runnable {

    public SpamRunnableOld(int reps, String message, TextChannel c, Long num) 
    {
        super(reps , message , c , num);
    }
    
    @Override
    public void run() 
    {
        for (int i = 0; i < this.getReps(); i++) 
        {
            if (this.isStop())
            {
                this.alertListeners();
                return;
            }

            getChannel().sendMessage(getMessage()).queue();

            cooldown();
        }
        this.alertListeners();
    }
}
