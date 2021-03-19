package frontsnapk1ck.alloy.command.economy;

import java.util.List;

import frontsnapk1ck.alloy.command.util.AbstractCooldownCommand;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.gameobjects.player.Player;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Queueable;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.intefs.handler.CooldownHandler;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.disterface.util.template.Template;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class WorkCommand extends AbstractCooldownCommand {

    public static final int MIN_WORK = 30;
    public static final int WORK_RANGE = 120;

    @Override
    public void execute(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        CooldownHandler handler = data.getCooldownHandler();
        Member m = g.getMember(author);
        Queueable q = data.getQueue();

        if (userOnCooldown(author, g, handler)) {
            Template t = Templates.onCooldown(m);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        int amt = getAmt();
        String option = getOption(g);
        Player p = AlloyUtil.loadPlayer(g, m);
        p.addBal(amt);

        Template t = Templates.workSuccess(option, amt);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);

        addUserCooldown(m, g, handler, getCooldownTime(g), q);

    }

    private String getOption(Guild g) {
        Server s = AlloyUtil.loadServer(g);
        List<String> workOptions = AlloyUtil.loadWorkOptions(s);

        int num = (int) (Math.random() * workOptions.size());
        return workOptions.get(num);
    }

    private int getAmt() {
        int amt = (int) (Math.random() * WORK_RANGE + MIN_WORK);
        return amt;
    }

}
