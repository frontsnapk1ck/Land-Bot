package frontsnapk1ck.alloy.main.intefs.handler;

import frontsnapk1ck.alloy.input.discord.AlloyInput;
import net.dv8tion.jda.api.entities.Guild;

public interface AlloyHandler {

    public void handleMessage(AlloyInput in);

    public void handleSlashMessage(AlloyInput in);

    public void guildCountUpdate();

	public void addGuildMap(Guild g);
    
}
