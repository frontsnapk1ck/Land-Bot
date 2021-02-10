package alloy.utility.job.jobs;

import alloy.command.economy.DayCommand;
import net.dv8tion.jda.api.JDA;
import utility.event.RepeatingJob;

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
