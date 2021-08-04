package frontsnapk1ck.alloy.utility.job;

import java.util.function.Consumer;

import frontsnapk1ck.utility.event.Result;
import frontsnapk1ck.utility.event.SimpleResult;

public class JobUtil {

    public static final Result<Void> VOID_RESULT;

    static {
        VOID_RESULT = configVoidResult();
    }

    private static Result<Void> configVoidResult()
    {
        SimpleResult<Void> result = new SimpleResult<Void>();
        result.finish(null);
        return result;
    }

    public static <T> JobConsumer<Void> parseJobConsumer(Consumer<? super T> action , T param)
    {
        return new JobConsumer<Void> ()
        {
            @Override
            public Result<Void> execute()
            {
                action.accept(param);
                return JobUtil.VOID_RESULT;
            }
        };
    }
}
