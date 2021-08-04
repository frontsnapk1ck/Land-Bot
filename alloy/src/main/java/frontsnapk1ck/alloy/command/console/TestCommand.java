package frontsnapk1ck.alloy.command.console;


import frontsnapk1ck.alloy.command.util.AbstractConsoleCommand;
import frontsnapk1ck.alloy.input.console.ConsoleInputData;
import frontsnapk1ck.alloy.utility.job.jobs.AlloyDelayJob;
import me.tongfei.progressbar.ProgressBar;

public class TestCommand extends AbstractConsoleCommand {

    @Override
    public void execute(ConsoleInputData data) 
    {
        AlloyDelayJob<String> j = new AlloyDelayJob<String>( (v) -> 
        {
            try
            (
                ProgressBar pb = new ProgressBar("Test", 10000)
            )
            {
                for( int i = 0; i < 10000; i++)
                {
                    try
                    {
                        Thread.sleep(1);
                    }
                    catch ( InterruptedException e) {                    
                    }
                    if (i % 2 == 0)
                        pb.setExtraMessage("reading number x 2");
                    else
                        pb.setExtraMessage("");
                    pb.step();
                }
            }
            catch (Exception e)
            {
                System.err.println(e.getMessage());
            }
            

        } , "");
        data.getQueue().queue(j);

    }
    
}
