package frontsnapk1ck.alloy.command.level;

import java.util.List;

import frontsnapk1ck.alloy.command.util.AbstractCooldownCommand;
import frontsnapk1ck.alloy.handler.command.EconHandler;
import frontsnapk1ck.alloy.handler.command.FunHandler;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class LeaderboardCommand extends AbstractCooldownCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        String[] args = AlloyInputUtil.getArgs(data);

        if (args.length == 0)
            rankLB(data);
        else if (   args[0].equalsIgnoreCase("cash") ||
                    args[0].equalsIgnoreCase("money") )
            rankMoney(data);
    }

    private void rankMoney(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        List<String> lb = EconHandler.loadLeaderboardMoney(g);
        AlloyTemplate t = Templates.leaderboard(lb);
        SendableMessage sm = new SendableMessage();
        sm.setFrom(getClass());
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

    private void rankLB(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        
        List<String> lb = FunHandler.loadLeaderboard(g);
        AlloyTemplate t = Templates.leaderboard(lb);
        SendableMessage sm = new SendableMessage();
        sm.setFrom(getClass());
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

}
