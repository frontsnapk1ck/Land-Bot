package alloy.main.handler;

import alloy.input.discord.AlloyInput;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

public interface AlloyHandler {

    public void handleMessage(AlloyInput in);

    public void handlePrivateMessage(PrivateChannel channel, User author, Message message);
    
}
