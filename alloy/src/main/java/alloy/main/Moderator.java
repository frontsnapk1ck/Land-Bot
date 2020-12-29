package alloy.main;

import net.dv8tion.jda.api.entities.TextChannel;

public interface Moderator {

	TextChannel getModLogChannel(long idLong);

}
