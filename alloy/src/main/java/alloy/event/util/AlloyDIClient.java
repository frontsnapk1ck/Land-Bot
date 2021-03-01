package alloy.event.util;

import java.io.IOException;

import alloy.main.Alloy;
import disterface.DisInterClient;
import disterface.util.DIUtil;

public class AlloyDIClient extends DisInterClient {

    public AlloyDIClient() throws IOException 
    {
        super();
    }

    @Override
    protected void tellConnected() 
    {
        Alloy.LOGGER.info("AlloyDIClient", "client connected to " + DIUtil.ADDRESS.getHostAddress() + " on port " + DIUtil.PORT );
    }

    @Override
    protected void tellConnecting() 
    {
        Alloy.LOGGER.debug("AlloyDIClient" , "client is connecting to " + DIUtil.ADDRESS.getHostAddress() + " on port " + DIUtil.PORT );
    }
    
}
