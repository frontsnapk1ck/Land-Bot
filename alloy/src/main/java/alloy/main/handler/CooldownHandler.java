package alloy.main.handler;

import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public interface CooldownHandler {

    public void addCooldownUser( Member m  );

    public void addXpCooldownUser( Member m  );

    public boolean removeCooldownUser( Member m );

    public boolean removeXpCooldownUser( Member m );

    public List<Long> getCooldownUsers( Guild g );

    public List<Long> getXpCooldownUsers( Guild g );
    
}
