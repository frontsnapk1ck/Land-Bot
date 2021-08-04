package frontsnapk1ck.alloy.utility.job.jobs;

import java.util.function.Consumer;

public class AlloyDelayJob<T> extends DelayJob<Void> {

    public AlloyDelayJob(Consumer<? super T> action, T param)
    {
        super(action, param , null);
    }
    
}
