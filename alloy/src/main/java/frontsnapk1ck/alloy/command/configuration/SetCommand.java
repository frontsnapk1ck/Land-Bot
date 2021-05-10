package frontsnapk1ck.alloy.command.configuration;

import java.net.MalformedURLException;
import java.net.URL;

import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.handler.command.FunHandler;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.discord.DisUtil;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import frontsnapk1ck.utility.Util;

public class SetCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission()
    {
        return DisPerm.ADMINISTRATOR;
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);

        if (!DisPermUtil.checkPermission(m, getPermission()))
        {
            AlloyTemplate t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args.length < 1) 
        {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args[0].equalsIgnoreCase("spam"))
            setSpam(data);
        else if (args[0].equalsIgnoreCase("xp"))
            setXp(data);
        else if (args[0].equalsIgnoreCase("bal"))
            setBal(data);
        else if (args[0].equalsIgnoreCase("admin-bypass"))
            adminBypass(data);
        else if (args[0].equalsIgnoreCase("mod-log"))
            modLog(data);
        else if (args[0].equalsIgnoreCase("mute"))
            mute(data);
        else if (args[0].equalsIgnoreCase("appeal"))
            appeal(data);
        else if (args[0].equalsIgnoreCase("econ-role"))
            econRole(data);
            
    }
    

    private void setBal(AlloyInputData data)
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        User tarU = DisUtil.parseUser(args[1]);

        if (args.length < 3) 
        {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (tarU == null)
        {
            AlloyTemplate t = Templates.userNotFound(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Member target = g.getMember(tarU);

        if (target == null)
        {
            AlloyTemplate t = Templates.userNotFound(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!Util.validInt(args[2]))
        {
            AlloyTemplate t = Templates.invalidNumberFormat(args[2]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        int bal = Integer.parseInt(args[2]);

        FunHandler.setBal(target, bal);
        AlloyTemplate t = Templates.balSetSuccess(target, bal);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        return;
	}

	private void econRole(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (!DisPermUtil.checkPermission(g.getSelfMember(), DisPerm.MANAGE_ROLES))
        {
            AlloyTemplate t = Templates.noPermission(DisPerm.MANAGE_ROLES, g.getSelfMember().getUser());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }

        Server s = AlloyUtil.loadServer(g);
        if (!DisPermUtil.checkPermission(g.getSelfMember(), DisPerm.MANAGE_ROLES))
        {
            s.changeAssignRolesOnBuy(false);

            AlloyTemplate t = Templates.assignRolesOnBuy(false);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
        
        if (args.length < 2) 
        {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String bool = args[1];
        if (bool.equalsIgnoreCase("false") || bool.equalsIgnoreCase("off"))
        {
            s.changeAssignRolesOnBuy(false);

            AlloyTemplate t = Templates.assignRolesOnBuy(false);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }
        else if (bool.equalsIgnoreCase("true") || bool.equalsIgnoreCase("on"))
        {
            s.changeAssignRolesOnBuy(true);

            AlloyTemplate t = Templates.assignRolesOnBuy(true);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }
        else
        {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }
    }

    private void appeal(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        
        if (args.length < 2) 
        {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        try 
        {
            if (!args[1].equalsIgnoreCase("none"))
                new URL(args[1]);
        }
        catch (MalformedURLException e) 
        {
            AlloyTemplate t = Templates.invalidURL(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
		}

        Server s = AlloyUtil.loadServer(g);
        s.setBanAppealLink(args[1]);

        AlloyTemplate t = Templates.banAppealChanged(args[1]);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom(getClass());
        bot.send(sm);
    }

    private void mute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        
        if (args.length < 2) 
        {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String role = args[1];

        Role r = DisUtil.parseRole( role , g );
        if (r == null && !role.equalsIgnoreCase("none"))
        {
            AlloyTemplate t = Templates.roleNotFound(role);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            sm.setFrom(getClass());
            bot.send(sm);
            return;
        }

        Server s = AlloyUtil.loadServer(g);
        s.setMuteRole(r.getIdLong());

        AlloyTemplate t = Templates.muteRoleChanged(role);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom(getClass());
        bot.send(sm);
    }

    private void modLog(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        
        if (args.length < 2) 
        {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
        
        String channelT = args[1];
        Server s = AlloyUtil.loadServer(g);

        TextChannel tc = DisUtil.findChannel(g, channelT);
        if (tc == null && !channelT.equalsIgnoreCase("none"))
        {
            AlloyTemplate t = Templates.channelNotFound(channelT);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            sm.setFrom(getClass());
            bot.send(sm);
            return;
        }
        
        try 
        {
            s.setModLog(tc.getIdLong());
        } 
        catch (NullPointerException e) 
        {
            s.setModLog(0);
        }

        AlloyTemplate t = Templates.modLogChanged(channelT);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom(getClass());
        bot.send(sm);
    }

    private void adminBypass(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        
        if (args.length < 2) 
        {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String bool = args[1];
        Server s = AlloyUtil.loadServer(g);
        if (bool.equalsIgnoreCase("false") || bool.equalsIgnoreCase("off"))
        {
            s.changeAdminCooldownBypass(false);

            AlloyTemplate t = Templates.adminBypassCooldown(false);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }
        else if (bool.equalsIgnoreCase("true") || bool.equalsIgnoreCase("on"))
        {
            s.changeAdminCooldownBypass(true);

            AlloyTemplate t = Templates.adminBypassCooldown(true);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }
        else
        {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }


    }

    private void setXp(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        User tarU = DisUtil.parseUser(args[1]);

        if (args.length < 3) 
        {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (tarU == null)
        {
            AlloyTemplate t = Templates.userNotFound(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Member target = g.getMember(tarU);

        if (target == null)
        {
            AlloyTemplate t = Templates.userNotFound(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!Util.validInt(args[2]))
        {
            AlloyTemplate t = Templates.invalidNumberFormat(args[2]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        int xp = Integer.parseInt(args[2]);

        FunHandler.setXP(target, xp);
        AlloyTemplate t = Templates.xpSetSuccess(target, xp);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        return;

    }

    private void setSpam(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (args.length < 2)
        {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        TextChannel target = DisUtil.findChannel(g, DisUtil.mentionToId(args[1]));
        if (target == null)
        {
            AlloyTemplate t = Templates.invalidChannel(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Server s = AlloyUtil.loadServer(g);
        s.changeSpamChannel(target.getIdLong());
        AlloyTemplate t = Templates.spamChannelChanged(target);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);

    }

}
