package alloy.builder.loaders.util;

import alloy.main.Alloy;

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
