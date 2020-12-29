package alloy.command.level;

import java.util.List;

import alloy.command.util.AbstractCooldownCommand;
import alloy.handler.RankHandeler;
import alloy.input.discord.AlloyInputData;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class LeaderboardCommand extends AbstractCooldownCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        List<String> lb = RankHandeler.loadLeaderboard(g);
        Template t = Templates.leaderboard( lb );
        SendableMessage sm = new SendableMessage();
        sm.setFrom("LeaderboardCommand");
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }
    
    
    
}
