package frontsnapk1ck.alloy.command.console;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;

import frontsnapk1ck.alloy.command.util.AbstractConsoleCommand;
import frontsnapk1ck.alloy.input.console.ConsoleInputData;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import frontsnapk1ck.utility.StringUtil;
import frontsnapk1ck.utility.Util;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.managers.RoleManager;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.internal.managers.RoleManagerImpl;

public class RolesCommand extends AbstractConsoleCommand {

    @Override
    public void execute(ConsoleInputData data) 
    {

        List<String> args = data.getArgs();
        JDA jda = data.getJda();

        if (args == null || args.size() == 0)
            return;

        String command = args.get(0);

        if (command.equalsIgnoreCase("strip"))
            stripUser(args, jda);
        if (command.equalsIgnoreCase("add"))
            addUser(args, jda);
        if (command.equalsIgnoreCase("remove"))
            removeUser(args, jda);
        if (command.equalsIgnoreCase("roles"))
            listServer(args, jda);
        if (command.equalsIgnoreCase("role"))
            role(args , jda);
        if (command.equalsIgnoreCase("selfPerms"))
            listSelf(args, jda);
        if (command.equalsIgnoreCase("userPerms"))
            listUser(args, jda);
    }

    private void role(List<String> args, JDA jda) 
    {
        if (args.size() != 6)
            return;
        if (args.get(2).equalsIgnoreCase("add"))
            roleAdd(args, jda);
        if (args.get(2).equalsIgnoreCase("remove"))
            roleRemove(args , jda);
    }

    private void roleRemove(List<String> args, JDA jda) 
    {
        try 
        {
            Guild g = jda.getGuildById(args.get(3));
            Role r = g.getRoleById(args.get(4));
            DisPerm p = DisPermUtil.parse(args.get(5));
            RoleManager rm = new RoleManagerImpl(r);
            rm.revokePermissions(DisPermUtil.parseStockPerm(p)).complete();
            
            System.out.println("Removed Permission: " + p.getName() + " to role: " + r.getName());
        }
        catch (Exception e) 
        {
            System.out.println("Failed with message: " + e.getMessage());
        }
    }

    private void roleAdd(List<String> args, JDA jda) 
    {
        try 
        {
            Guild g = jda.getGuildById(args.get(3));
            Role r = g.getRoleById(args.get(4));
            DisPerm p = DisPermUtil.parse(args.get(5));
            RoleManager rm = new RoleManagerImpl(r);
            rm.givePermissions(DisPermUtil.parseStockPerm(p)).complete();
            
            System.out.println("Added Permission: " + p.getName() + " to role: " + r.getName());
        }
        catch (Exception e) 
        {
            System.out.println("Failed with message: " + e.getMessage());
        }
    }

    private void listServer(List<String> args, JDA jda) {
        String id = args.get(1);
        Guild g = jda.getGuildById(id);
        List<Role> roles = g.getRoles();
        String[][] data = new String[roles.size()][];

        int i = 0;
        for (Role r : roles) {
            boolean isAdmin = r.getPermissions().contains(Permission.ADMINISTRATOR);
            data[i] = new String[] { r.getName(), r.getId(), "" + isAdmin, makePermString(r.getPermissions()), };
            i++;
        }

        String[] headers = new String[] { "~~Role Name~~", "~~Role ID~~", "~~Is ADMIN~~", "~~PERMS~~" };
        String table = StringUtil.makeTable(data, headers);
        System.out.println("Roles in guild " + g.getName());
        System.out.println(table);
    }

    private static String makePermString(EnumSet<Permission> permissions) {
        String out = "[";

        List<Permission> perms = new ArrayList<Permission>(permissions);

        final int MAX_NUM = 5;
        for (int i = 0; i < perms.size() && i < MAX_NUM; i++) {
            Permission p = perms.get(i);

            if (i != permissions.size() - 1 && i != MAX_NUM - 1)
                out += p + ", ";
            else
                out += p;
        }

        out += "]";
        return out;
    }

    private void listUser(List<String> args, JDA jda) {
        String gid = args.get(1);
        String mid = args.get(2);

        Guild g = jda.getGuildById(gid);
        Member m = g.getMemberById(mid);
        listPerms(m);
    }

    private void listSelf(List<String> args, JDA jda) {
        String id = args.get(1);
        Guild g = jda.getGuildById(id);
        Member m = g.getSelfMember();
        listPerms(m);
    }

    private static void listPerms(Member m) {
        EnumSet<Permission> perms = m.getPermissions();
        for (Permission p : perms)
            System.out.println(p);
    }

    private void addUser(List<String> args, JDA jda) 
    {
        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>() 
        {
            @Override
            public void accept(ErrorResponseException t) 
            {
                Alloy.LOGGER.warn("BanCommand", t.getMessage());
            }

            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.UNKNOWN_USER, consumer);

        if (args.size() != 4)
            return;
        String gid = args.get(1);
        String mid = args.get(2);
        String rid = args.get(3);
        if (gid == null || mid == null || rid == null)
            return;
        Guild g = jda.getGuildById(gid);
        Member m = g.getMemberById(mid);

        Role r = g.getRoleById(rid);
        g.addRoleToMember(m, r).queue(null,handler);
    }

    private void removeUser(List<String> args, JDA jda) 
    {
        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>() 
        {
            @Override
            public void accept(ErrorResponseException t) 
            {
                Alloy.LOGGER.warn("BanCommand", t.getMessage());
            }

            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.UNKNOWN_USER, consumer);

        if (args.size() != 4)
            return;
        String gid = args.get(1);
        String mid = args.get(2);
        String rid = args.get(3);
        if (gid == null || mid == null || rid == null)
            return;
        Guild g = jda.getGuildById(gid);
        Member m = g.getMemberById(mid);

        Role r = g.getRoleById(rid);
        g.removeRoleFromMember(m, r).queue(null,handler);
    }

    private void stripUser(List<String> args, JDA jda) 
    {
        List<Guild> all = jda.getGuilds();
        if (args.size() != 3)
            return;
        String gid = args.get(1).trim();
        String mid = args.get(2).trim();
        if (gid == null || mid == null)
            return;
        Guild g = getGuild(all, gid);
        Member m = getMember(g, mid);
        if (m == null) {
            List<Member> members = g.getManager().getGuild().getMembers();
            for (Member member : members) {
                if (member.getId().equalsIgnoreCase(mid))
                    ;
                m = member;
            }
        }

        List<Role> roles = m.getRoles();

        String[] headers = new String[] { "~~Role Name~~", "~~Role ID~~", "~~Add Back Command~~", };
        String[][] data = new String[0][];

        int i = 0;

        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>() 
        {
            @Override
            public void accept(ErrorResponseException t) 
            {
                Alloy.LOGGER.warn("RolesCommand", t.getMessage());
            }

            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.MISSING_PERMISSIONS, consumer);

        for (Role r : roles) 
        {
            try {
                g.removeRoleFromMember(m, r).queue(null , handler);
                data = Util.addOneToArray(data);
                String addBack = "add " + gid + " " + mid + " " + r.getId();
                data[i] = new String[] { r.getName(), r.getId(), addBack, };
                i++;
            } catch (Exception e) {
                Alloy.LOGGER.warn("RolesCommand", e.getMessage());
            }
        }
        System.out.println("roles removed from user\t\t" + m.getEffectiveName() + "\t\t" + m.getUser().getAsTag());
        if (data.length == 0)
            System.out.println("NONE");
        else
            System.out.println(StringUtil.makeTable(data, headers));
    }

    private Guild getGuild(List<Guild> all, String gid) {
        long id = Long.parseLong(gid);
        for (Guild guild : all) {
            if (guild.getIdLong() == id)
                return guild;
        }
        return null;
    }

    private Member getMember(Guild g, String mid) {
        long id = Long.parseLong(mid);
        for (Member member : g.getMembers()) {
            if (member.getIdLong() == id)
                return member;
        }
        return null;
    }

}
