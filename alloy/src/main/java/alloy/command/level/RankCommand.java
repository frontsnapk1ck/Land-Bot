package alloy.command.level;

import alloy.command.util.AbstractCommand;
import alloy.handler.command.RankHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.templates.Templates;
import disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class RankCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);

        Member target = m;

        if (args.length != 0)
            target = RankHandler.findUser(args[0], bot, channel);

        if (target == null) {
            Template t = Templates.userNotFound(args[0]);
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }

        RankHandler.seeRank(target, channel, bot);

    }

}
