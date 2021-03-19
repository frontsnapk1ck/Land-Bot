package frontsnapk1ck.alloy.utility.job.jobs;

import frontsnapk1ck.alloy.command.economy.DayCommand;
import net.dv8tion.jda.api.JDA;
import frontsnapk1ck.utility.event.RepeatingJob;

public class DayJob extends RepeatingJob {

    private JDA jda;

    public DayJob(JDA jda) 
    {
        super(86400000L);
        this.jda = jda;
    }

    @Override
    public void execute() 
    {
        DayCommand.dayAll(jda.getGuilds());
    }
    
}
