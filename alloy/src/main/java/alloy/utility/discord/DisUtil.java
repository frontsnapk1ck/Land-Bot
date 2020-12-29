package alloy.utility.discord;

import java.util.ArrayList;
import java.util.List;

import alloy.utility.error.InvalidUserFormat;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class DisUtil {

    public static Member findMember(Guild g, String[] args) 
    {
        for (String s : args) 
        {
            Member m = findMember(g, s);
            if ( m != null)
                return m;
        }

        return null;
    }

    public static Member findMember( Guild g , String member )
    {
        if (member.startsWith("<@") && member.endsWith(">")) 
        {
            String id = member.replace("<@", "");
            id = id.replace("!", "");
            id = id.replace(">", "");

            User u = User.fromId(id);
            return g.getMember(u);
        }
        return null;
    }

    public static TextChannel findChannel(Guild guild, String id) 
    {
        for (GuildChannel c : guild.getChannels() )
        {
            if (c.getId().equals(id))
                return (TextChannel) c;    
        }
        return null;
    }

    public static TextChannel findChannel(Guild guild, long id) 
    {
        for (GuildChannel c : guild.getChannels() )
        {
            if (c.getIdLong() == id)
                return (TextChannel) c;    
        }
        return null;
	}

    public static User parseUser(String user) throws InvalidUserFormat
    {
        String id = user.replace("<@!", "");
        id = id.replace("<@", "");
        id = id.replace(">","");

        User u = User.fromId(id);
        if (u == null)
            throw new InvalidUserFormat(user + " is an invalid user format");
        return u;       
	}


    public static boolean isUserMention(String string) 
    {
        return (string.contains("<@") || string.contains("<@!")) && string.contains(">");
	}

    public static Member findUserIn(TextChannel channel, String searchText) 
    {
        List<Member> users = channel.getGuild().getMembers();
        List<Member> potential = new ArrayList<>();
        int smallestDiffIndex = 0;
        int smallestDiff = -1;

        for (Member u : users) 
        {
            String nick = u.getEffectiveName();
            if (nick.equalsIgnoreCase(searchText))
                return u;
            if (nick.toLowerCase().contains(searchText)) 
            {
                potential.add(u);
                int d = Math.abs(nick.length() - searchText.length());
                
                if (d < smallestDiff || smallestDiff == -1) 
                {
                    smallestDiff = d;
                    smallestDiffIndex = potential.size() - 1;
                }
            }
        }
        if (!potential.isEmpty()) 
        {
            return potential.get(smallestDiffIndex);
        }
        return null;
    }

    public static long mentionToId(String s) 
    {
		String  id = s.replace("<@", "");
                id = id.replace("!", "");
                id = id.replace("<#", "");
                id = id.replace("<&" , "");
                id = id.replace(">", "");

        return Long.parseLong(id);
	}

    public static Role parseRole(String role, Guild g) 
    {
        List<Role> roles = g.getRoles();
        for (Role r : roles) 
        {
            boolean valid = r.getAsMention().equalsIgnoreCase(role) ||
                            r.getId().equalsIgnoreCase(role);
            if (valid) 
                return r;
        }
        return null;
	}

    public static boolean isValidChannel(Guild g, String channel) 
    {
        channel = channel.replace("<#", "");
        channel = channel.replace(">", "");

        GuildChannel c = g.getGuildChannelById(channel);
        return c != null;

	}

    public static boolean isRole(String role, Guild g) 
    {
        if (!role.startsWith("<@&") || !role.endsWith(">"))
            return false;
		role = role.replace("<@&", "");
        role = role.replace(">", "");

        Role r = g.getRoleById(role);
        return r != null;
	}

    public static String trimMention(String mention) 
    {
        mention = mention.replace("<@&", "");
        mention = mention.replace("<#", "");
        mention = mention.replace("<@!", "");
        mention = mention.replace("<@", "");

        mention = mention.replace(">", "");

        return mention;

	}
    
}
