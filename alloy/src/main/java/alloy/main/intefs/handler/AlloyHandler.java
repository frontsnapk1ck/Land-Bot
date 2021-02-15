package alloy.main.intefs.handler;

import alloy.input.discord.AlloyInput;

public interface AlloyHandler {

    public void handleMessage(AlloyInput in);

    public void guildCountUpdate();
    
}
