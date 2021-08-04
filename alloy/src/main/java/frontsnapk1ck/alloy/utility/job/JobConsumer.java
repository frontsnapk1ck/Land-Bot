package frontsnapk1ck.alloy.utility.job;

import frontsnapk1ck.utility.event.Result;

public abstract class JobConsumer<T> {

    public abstract Result<T> execute();

}
