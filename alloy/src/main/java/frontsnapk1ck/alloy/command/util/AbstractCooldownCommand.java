package frontsnapk1ck.alloy.command.util;

import java.util.List;

import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.main.intefs.Queueable;
import frontsnapk1ck.alloy.main.intefs.handler.CooldownHandler;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import frontsnapk1ck.alloy.utility.job.jobs.AddUserCoolDownJob;
import frontsnapk1ck.alloy.utility.job.jobs.RmUserCoolDownJob;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import frontsnapk1ck.utility.event.Job;

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

    protected static void addUserCooldown(Member member, Guild guild, CooldownHandler handler , long seconds, Queueable q) 
    {
        Server s = AlloyUtil.loadServer(guild);
        if (s.getAdminCooldownBypass() && DisPermUtil.checkPermission(member, DisPerm.ADMINISTRATOR))
            return;

        User author = member.getUser();
        
        Job j = new AddUserCoolDownJob(guild , author , handler );
        q.queue(j);
        Job j2 = new RmUserCoolDownJob(guild, author, handler);
        q.queueIn(j2, seconds * 1000l);
    }

}
