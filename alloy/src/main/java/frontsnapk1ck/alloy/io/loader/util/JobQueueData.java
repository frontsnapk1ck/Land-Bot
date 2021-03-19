package frontsnapk1ck.alloy.io.loader.util;

import frontsnapk1ck.alloy.main.Alloy;

public class JobQueueData {

    public Alloy alloy;
    public String file;
    
    public JobQueueData(Alloy alloy , String file) 
    {
        super();
        this.file = file;
        this.alloy = alloy;
    }
}
