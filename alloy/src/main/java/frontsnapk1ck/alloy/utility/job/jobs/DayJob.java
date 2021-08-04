package frontsnapk1ck.alloy.utility.job.jobs;

import frontsnapk1ck.alloy.command.economy.DayCommand;
import frontsnapk1ck.alloy.utility.job.JobUtil;
import frontsnapk1ck.utility.event.RepeatingJob;
import frontsnapk1ck.utility.event.Result;
import net.dv8tion.jda.api.JDA;

public class DayJob extends RepeatingJob {

    private JDA jda;

    public DayJob(JDA jda) 
    {
        super(86400000L);
        this.jda = jda;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result<Void> execute() 
    {
        DayCommand.dayAll(jda.getGuilds());
        return JobUtil.VOID_RESULT;
    }
    
}
