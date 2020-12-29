package alloy.main;

import net.dv8tion.jda.api.requests.restaction.MessageAction;

public interface Sendable {

    public void send(SendableMessage message);

    public MessageAction getAction( SendableMessage message );
    
}
