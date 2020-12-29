package alloy.handler;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import alloy.main.Alloy;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import utility.StringUtil;
import utility.Util;

public class InputHandeler {

    public static void handle(JDA jda, String input) {
        String[] args = input.split(" ");
        if (args[0].equalsIgnoreCase("guild"))
            guildID(args[1], jda);
        if (args[0].equalsIgnoreCase("strip"))
            strip(args, jda);
        if (args[0].equalsIgnoreCase("add"))
            add(args, jda);
        if (args[0].equalsIgnoreCase("members"))
            members(args, jda);
        if (args[0].equalsIgnoreCase("user"))
            user(args[1], jda);
        if (args[0].equalsIgnoreCase("getInvites"))
            getInvites(jda);
        if (args[0].equalsIgnoreCase("roles"))
            roles(args[1], jda);
        if (args[0].equalsIgnoreCase("selfPerms"))
            selfPerms(args[1], jda);

    }

    private static void selfPerms(String id, JDA jda) 
    {
        Guild g = jda.getGuildById(id);
        Member m = g.getSelfMember();
        listPerms(m);
    }

    private static void listPerms(Member m) 
    {
        EnumSet<Permission> perms = m.getPermissions();
        for (Permission p : perms) 
            System.err.println(p);
    }

    private static void roles(String id, JDA jda) 
    {
        Guild g = jda.getGuildById(id);
        List<Role> roles = g.getRoles();
        String[][] data = new String[roles.size()][];

        int i = 0;
        for (Role r : roles) 
        {
            boolean isAdmin = r.getPermissions().contains(Permission.ADMINISTRATOR);
            data[i] = new String[] 
            {
                r.getName() ,
                r.getId() ,
                "" + isAdmin ,
                makePermString(r.getPermissions()) ,
            };
            i ++;
        }

        String[] headers = new String[] {
            "~~Role Name~~" ,
            "~~Role ID~~" ,
            "~~Is ADMIN~~" ,
            "~~PERMS~~"
        };
        String table = StringUtil.makeTable(data, headers);
        System.err.println("Roles in guild " + g.getName());
        System.err.println(table);
    }

    private static String makePermString(EnumSet<Permission> permissions) 
    {
        String out = "[";

        List<Permission> perms = new ArrayList<Permission>(permissions);

        final int MAX_NUM = 5;
        for (int i = 0; i < perms.size() && i < MAX_NUM; i++) 
        {
            Permission p = perms.get(i);

            if (i != permissions.size() -1 && i != MAX_NUM -1)
                out += p + ", ";
            else
                out += p;
        }

        out += "]";
        return out; 
    }

    private static void getInvites(JDA jda) 
    {
        List<Guild> guilds = jda.getGuilds();
        for (Guild g : guilds) 
        {
            String code = "CANNOT MAKE INVITE";
            try {
                code = g.getDefaultChannel().createInvite().complete().getCode();
            } catch (Exception e) {}
            System.out.println(g.getName() + "\t\tdiscord.gg/" + code);
        }
    }

    private static void user(String string, JDA jda) 
    {
        User u = User.fromId(string);
        System.err.println(u.getName());
    }

    private static void add(String[] args, JDA jda) 
    {
        if (args.length != 4)
            return;
        String gid = args[1];
        String mid = args[2];
        String rid = args[3];
        if (gid == null || mid == null || rid == null)
            return;
        Guild g = jda.getGuildById(gid);
        Member m = g.getMemberById(mid);

        Role r = g.getRoleById(rid);
        g.addRoleToMember(m, r).queue();
    }

    private static void strip(String[] args, JDA jda) 
    {
        String gid = args[1];
        String mid = args[2];
        if (gid == null || mid == null)
            return;
        Guild g = jda.getGuildById(gid);
        User u =User.fromId(mid);
        Member m = g.getMember(u);
        if ( m == null)
        {
            List<Member> members = g.getManager().getGuild().getMembers();
            for (Member member : members) 
            {
                if (member.getId().equalsIgnoreCase(mid));
                    m = member;
            }
        }

        List<Role> roles = m.getRoles();

        String[] headers = new String[]{
            "~~Role Name~~" ,
            "~~Role ID~~" , 
            "~~Add Back Command~~" ,
        };
        String[][] data = new String[0][];

        int i = 0;
        for (Role r : roles)
        {
            try 
            {
                g.removeRoleFromMember(m, r ).queue();
                data = Util.addOneToArray(data);
                String addback = "add " + gid + " " + mid + " "  + r.getId();
                data[i] = new String[]
                {
                    r.getName() , 
                    r.getId() ,
                    addback ,
                };
                i ++;
            } 
            catch (Exception e) 
            {
                Alloy.LOGGER.warn("InputHandeler", e.getMessage());
            }
        }
        System.out.println("roles removed from user\t\t" + m.getEffectiveName() + "\t\t" + m.getUser().getAsTag());
        if (data.length == 0)
            System.err.println("NONE");
        else
            System.err.println(StringUtil.makeTable(data, headers));
    }

    private static void guildID(String id, JDA jda) 
    {
        if (id == null)
            return;
        Guild g = jda.getGuildById(id);
        if (g == null)
            return;
        System.out.println(g.getName());
    }

    private static void members(String[] args, JDA jda)
    {
        Guild g = jda.getGuildById(args[1]);
        List<Member> memebers = g.getMembers();
        String[][] arr = new String[memebers.size()][3];
        
        String[] headers = {
            "~~nick~~" ,
            "~~tag~~" ,
            "~~id~~" ,
        };

        for (int i = 0; i < arr.length; i++) 
        {
            Member m = memebers.get(i);
            arr[i][0] = m.getEffectiveName();
            arr[i][1] = m.getUser().getAsTag();
            arr[i][2] = m.getId();
        }
        String table = StringUtil.makeTable(arr , headers);
        System.err.println(table);
    }
    
}
