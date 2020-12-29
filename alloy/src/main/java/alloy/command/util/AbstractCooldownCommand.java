package alloy.command.util;

import java.util.List;

import alloy.gameobjects.Server;
import alloy.main.Queueable;
import alloy.main.handler.CooldownHandler;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.job.AddUserCoolDownJob;
import alloy.utility.job.RmUserCoolDownJob;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import utility.event.Job;

public abstract class AbstractCooldownCommand extends AbstractCommand {

    public long getCooldownTime(Guild g) 
    {
        Server s = AlloyUtil.loadServer(g);
        return s.getWorkCooldown();
    }

    protected static boolean userOnCooldown(User author, Guild guild, CooldownHandler handler ) 
    {
        long userID = author.getIdLong();
        List<Long> cooldown = handler.getCooldownUsers(guild);
        for (long cooldownID : cooldown) 
        {
            if ( cooldownID == userID )
                return true;
        }
        return false;
    }

    protected static void addUserCooldown(User author, Guild guild, CooldownHandler handler , long seconds, Queueable q) 
    {

        Job j = new AddUserCoolDownJob(guild , author , handler );
        q.queue(j);
        Job j2 = new RmUserCoolDownJob(guild, author, handler);
        q.queueIn(j2, seconds * 1000l);
    }

}
