package frontsnapk1ck.alloy.utility.discord.perm;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.error.PermissionParseException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class DisPermUtil {

    private static final List<DisPerm> MOD_PERMS;
    private static final List<DisPerm> MANAGER_PERMS;

    static {
        MOD_PERMS = loadModPerms();
        MANAGER_PERMS = loadManagerPerms();
    }

    private static List<DisPerm> loadManagerPerms() {
        List<DisPerm> perms = new ArrayList<DisPerm>();

        perms.add(DisPerm.MANAGE_ROLES);
        perms.add(DisPerm.MANAGE_PERMISSIONS);
        perms.add(DisPerm.MANAGE_WEBHOOKS);
        perms.add(DisPerm.MANAGE_SERVER);
        perms.add(DisPerm.MANAGE_CHANNEL);

        return perms;

    }

    private static List<DisPerm> loadModPerms() 
    {
        List<DisPerm> perms = new ArrayList<DisPerm>();

        perms.add(DisPerm.KICK_MEMBERS);
        perms.add(DisPerm.BAN_MEMBERS);
        perms.add(DisPerm.MESSAGE_MANAGE);
        perms.add(DisPerm.VIEW_AUDIT_LOGS);
        perms.add(DisPerm.VOICE_MUTE_OTHERS);
        perms.add(DisPerm.VOICE_DEAF_OTHERS);
        perms.add(DisPerm.VOICE_MOVE_OTHERS);
        perms.add(DisPerm.NICKNAME_MANAGE);

        return perms;
        
    }

    public static Permission parseStockPerm(String perm) throws PermissionParseException {
        if (perm.equalsIgnoreCase("CREATE_INSTANT_INVITE"))
            return Permission.CREATE_INSTANT_INVITE;

        if (perm.equalsIgnoreCase("KICK_MEMBERS"))
            return Permission.KICK_MEMBERS;

        if (perm.equalsIgnoreCase("BAN_MEMBERS"))
            return Permission.BAN_MEMBERS;

        if (perm.equalsIgnoreCase("ADMINISTRATOR"))
            return Permission.ADMINISTRATOR;

        if (perm.equalsIgnoreCase("MANAGE_CHANNEL"))
            return Permission.MANAGE_CHANNEL;

        if (perm.equalsIgnoreCase("MANAGE_SERVER"))
            return Permission.MANAGE_SERVER;

        if (perm.equalsIgnoreCase("MESSAGE_ADD_REACTION"))
            return Permission.MESSAGE_ADD_REACTION;

        if (perm.equalsIgnoreCase("VIEW_AUDIT_LOGS"))
            return Permission.VIEW_AUDIT_LOGS;

        if (perm.equalsIgnoreCase("PRIORITY_SPEAKER"))
            return Permission.PRIORITY_SPEAKER;

        if (perm.equalsIgnoreCase("VIEW_GUILD_INSIGHTS"))
            return Permission.VIEW_GUILD_INSIGHTS;

        if (perm.equalsIgnoreCase("VIEW_CHANNEL"))
            return Permission.VIEW_CHANNEL;

        if (perm.equalsIgnoreCase("MESSAGE_READ"))
            return Permission.MESSAGE_READ;

        if (perm.equalsIgnoreCase("MESSAGE_WRITE"))
            return Permission.MESSAGE_WRITE;

        if (perm.equalsIgnoreCase("MESSAGE_TTS"))
            return Permission.MESSAGE_TTS;

        if (perm.equalsIgnoreCase("MESSAGE_MANAGE"))
            return Permission.MESSAGE_MANAGE;

        if (perm.equalsIgnoreCase("MESSAGE_EMBED_LINKS"))
            return Permission.MESSAGE_EMBED_LINKS;

        if (perm.equalsIgnoreCase("MESSAGE_ATTACH_FILES"))
            return Permission.MESSAGE_ATTACH_FILES;

        if (perm.equalsIgnoreCase("MESSAGE_HISTORY"))
            return Permission.MESSAGE_HISTORY;

        if (perm.equalsIgnoreCase("MESSAGE_MENTION_EVERYONE"))
            return Permission.MESSAGE_MENTION_EVERYONE;

        if (perm.equalsIgnoreCase("MESSAGE_EXT_EMOJI"))
            return Permission.MESSAGE_EXT_EMOJI;

        if (perm.equalsIgnoreCase("VOICE_STREAM"))
            return Permission.VOICE_STREAM;

        if (perm.equalsIgnoreCase("VOICE_CONNECT"))
            return Permission.VOICE_CONNECT;

        if (perm.equalsIgnoreCase("VOICE_SPEAK"))
            return Permission.VOICE_SPEAK;

        if (perm.equalsIgnoreCase("VOICE_MUTE_OTHERS"))
            return Permission.VOICE_MUTE_OTHERS;

        if (perm.equalsIgnoreCase("VOICE_DEAF_OTHERS"))
            return Permission.VOICE_DEAF_OTHERS;

        if (perm.equalsIgnoreCase("VOICE_MOVE_OTHERS"))
            return Permission.VOICE_MOVE_OTHERS;

        if (perm.equalsIgnoreCase("VOICE_USE_VAD"))
            return Permission.VOICE_USE_VAD;

        if (perm.equalsIgnoreCase("NICKNAME_CHANGE"))
            return Permission.NICKNAME_CHANGE;

        if (perm.equalsIgnoreCase("NICKNAME_MANAGE"))
            return Permission.NICKNAME_MANAGE;

        if (perm.equalsIgnoreCase("MANAGE_ROLES"))
            return Permission.MANAGE_ROLES;

        if (perm.equalsIgnoreCase("MANAGE_PERMISSIONS"))
            return Permission.MANAGE_PERMISSIONS;

        if (perm.equalsIgnoreCase("MANAGE_WEBHOOKS"))
            return Permission.MANAGE_WEBHOOKS;

        if (perm.equalsIgnoreCase("MANAGE_EMOTES"))
            return Permission.MANAGE_EMOTES;
        
        if (perm.equalsIgnoreCase("USE_SLASH_COMMANDS"))
            return Permission.USE_SLASH_COMMANDS;

        throw new PermissionParseException(perm);
    }

    public static Permission parseStockPerm(DisPerm perm) throws PermissionParseException {
        if (perm.getName().equalsIgnoreCase("CREATE_INSTANT_INVITE"))
            return Permission.CREATE_INSTANT_INVITE;

        if (perm.getName().equalsIgnoreCase("KICK_MEMBERS"))
            return Permission.KICK_MEMBERS;

        if (perm.getName().equalsIgnoreCase("BAN_MEMBERS"))
            return Permission.BAN_MEMBERS;

        if (perm.getName().equalsIgnoreCase("ADMINISTRATOR"))
            return Permission.ADMINISTRATOR;

        if (perm.getName().equalsIgnoreCase("MANAGE_CHANNEL"))
            return Permission.MANAGE_CHANNEL;

        if (perm.getName().equalsIgnoreCase("MANAGE_SERVER"))
            return Permission.MANAGE_SERVER;

        if (perm.getName().equalsIgnoreCase("MESSAGE_ADD_REACTION"))
            return Permission.MESSAGE_ADD_REACTION;

        if (perm.getName().equalsIgnoreCase("VIEW_AUDIT_LOGS"))
            return Permission.VIEW_AUDIT_LOGS;

        if (perm.getName().equalsIgnoreCase("PRIORITY_SPEAKER"))
            return Permission.PRIORITY_SPEAKER;

        if (perm.getName().equalsIgnoreCase("VIEW_GUILD_INSIGHTS"))
            return Permission.VIEW_GUILD_INSIGHTS;

        if (perm.getName().equalsIgnoreCase("VIEW_CHANNEL"))
            return Permission.VIEW_CHANNEL;

        if (perm.getName().equalsIgnoreCase("MESSAGE_READ"))
            return Permission.MESSAGE_READ;

        if (perm.getName().equalsIgnoreCase("MESSAGE_WRITE"))
            return Permission.MESSAGE_WRITE;

        if (perm.getName().equalsIgnoreCase("MESSAGE_TTS"))
            return Permission.MESSAGE_TTS;

        if (perm.getName().equalsIgnoreCase("MESSAGE_MANAGE"))
            return Permission.MESSAGE_MANAGE;

        if (perm.getName().equalsIgnoreCase("MESSAGE_EMBED_LINKS"))
            return Permission.MESSAGE_EMBED_LINKS;

        if (perm.getName().equalsIgnoreCase("MESSAGE_ATTACH_FILES"))
            return Permission.MESSAGE_ATTACH_FILES;

        if (perm.getName().equalsIgnoreCase("MESSAGE_HISTORY"))
            return Permission.MESSAGE_HISTORY;

        if (perm.getName().equalsIgnoreCase("MESSAGE_MENTION_EVERYONE"))
            return Permission.MESSAGE_MENTION_EVERYONE;

        if (perm.getName().equalsIgnoreCase("MESSAGE_EXT_EMOJI"))
            return Permission.MESSAGE_EXT_EMOJI;

        if (perm.getName().equalsIgnoreCase("VOICE_STREAM"))
            return Permission.VOICE_STREAM;

        if (perm.getName().equalsIgnoreCase("VOICE_CONNECT"))
            return Permission.VOICE_CONNECT;

        if (perm.getName().equalsIgnoreCase("VOICE_SPEAK"))
            return Permission.VOICE_SPEAK;

        if (perm.getName().equalsIgnoreCase("VOICE_MUTE_OTHERS"))
            return Permission.VOICE_MUTE_OTHERS;

        if (perm.getName().equalsIgnoreCase("VOICE_DEAF_OTHERS"))
            return Permission.VOICE_DEAF_OTHERS;

        if (perm.getName().equalsIgnoreCase("VOICE_MOVE_OTHERS"))
            return Permission.VOICE_MOVE_OTHERS;

        if (perm.getName().equalsIgnoreCase("VOICE_USE_VAD"))
            return Permission.VOICE_USE_VAD;

        if (perm.getName().equalsIgnoreCase("NICKNAME_CHANGE"))
            return Permission.NICKNAME_CHANGE;

        if (perm.getName().equalsIgnoreCase("NICKNAME_MANAGE"))
            return Permission.NICKNAME_MANAGE;

        if (perm.getName().equalsIgnoreCase("MANAGE_ROLES"))
            return Permission.MANAGE_ROLES;

        if (perm.getName().equalsIgnoreCase("MANAGE_PERMISSIONS"))
            return Permission.MANAGE_PERMISSIONS;

        if (perm.getName().equalsIgnoreCase("MANAGE_WEBHOOKS"))
            return Permission.MANAGE_WEBHOOKS;

        if (perm.getName().equalsIgnoreCase("MANAGE_EMOTES"))
            return Permission.MANAGE_EMOTES;
        
        if (perm.getName().equalsIgnoreCase("USE_SLASH_COMMANDS"))
            return Permission.USE_SLASH_COMMANDS;

        throw new PermissionParseException(perm);
    }

    public static DisPerm parse(String perm) throws PermissionParseException {
        if (perm.equalsIgnoreCase("CREATE_INSTANT_INVITE"))
            return DisPerm.CREATE_INSTANT_INVITE;

        if (perm.equalsIgnoreCase("KICK_MEMBERS"))
            return DisPerm.KICK_MEMBERS;

        if (perm.equalsIgnoreCase("BAN_MEMBERS"))
            return DisPerm.BAN_MEMBERS;

        if (perm.equalsIgnoreCase("ADMINISTRATOR"))
            return DisPerm.ADMINISTRATOR;

        if (perm.equalsIgnoreCase("MANAGE_CHANNEL"))
            return DisPerm.MANAGE_CHANNEL;

        if (perm.equalsIgnoreCase("MANAGE_SERVER"))
            return DisPerm.MANAGE_SERVER;

        if (perm.equalsIgnoreCase("MESSAGE_ADD_REACTION"))
            return DisPerm.MESSAGE_ADD_REACTION;

        if (perm.equalsIgnoreCase("VIEW_AUDIT_LOGS"))
            return DisPerm.VIEW_AUDIT_LOGS;

        if (perm.equalsIgnoreCase("PRIORITY_SPEAKER"))
            return DisPerm.PRIORITY_SPEAKER;

        if (perm.equalsIgnoreCase("VIEW_GUILD_INSIGHTS"))
            return DisPerm.VIEW_GUILD_INSIGHTS;

        if (perm.equalsIgnoreCase("VIEW_CHANNEL"))
            return DisPerm.VIEW_CHANNEL;

        if (perm.equalsIgnoreCase("MESSAGE_READ"))
            return DisPerm.MESSAGE_READ;

        if (perm.equalsIgnoreCase("MESSAGE_WRITE"))
            return DisPerm.MESSAGE_WRITE;

        if (perm.equalsIgnoreCase("MESSAGE_TTS"))
            return DisPerm.MESSAGE_TTS;

        if (perm.equalsIgnoreCase("MESSAGE_MANAGE"))

            return DisPerm.MESSAGE_MANAGE;

        if (perm.equalsIgnoreCase("MESSAGE_EMBED_LINKS"))
            return DisPerm.MESSAGE_EMBED_LINKS;

        if (perm.equalsIgnoreCase("MESSAGE_ATTACH_FILES"))
            return DisPerm.MESSAGE_ATTACH_FILES;

        if (perm.equalsIgnoreCase("MESSAGE_HISTORY"))
            return DisPerm.MESSAGE_HISTORY;

        if (perm.equalsIgnoreCase("MESSAGE_MENTION_EVERYONE"))
            return DisPerm.MESSAGE_MENTION_EVERYONE;

        if (perm.equalsIgnoreCase("MESSAGE_EXT_EMOJI"))
            return DisPerm.MESSAGE_EXT_EMOJI;

        if (perm.equalsIgnoreCase("VOICE_STREAM"))
            return DisPerm.VOICE_STREAM;

        if (perm.equalsIgnoreCase("VOICE_CONNECT"))
            return DisPerm.VOICE_CONNECT;

        if (perm.equalsIgnoreCase("VOICE_SPEAK"))
            return DisPerm.VOICE_SPEAK;

        if (perm.equalsIgnoreCase("VOICE_MUTE_OTHERS"))
            return DisPerm.VOICE_MUTE_OTHERS;

        if (perm.equalsIgnoreCase("VOICE_DEAF_OTHERS"))
            return DisPerm.VOICE_DEAF_OTHERS;

        if (perm.equalsIgnoreCase("VOICE_MOVE_OTHERS"))
            return DisPerm.VOICE_MOVE_OTHERS;

        if (perm.equalsIgnoreCase("VOICE_USE_VAD"))
            return DisPerm.VOICE_USE_VAD;

        if (perm.equalsIgnoreCase("NICKNAME_CHANGE"))
            return DisPerm.NICKNAME_CHANGE;

        if (perm.equalsIgnoreCase("NICKNAME_MANAGE"))
            return DisPerm.NICKNAME_MANAGE;

        if (perm.equalsIgnoreCase("MANAGE_ROLES"))
            return DisPerm.MANAGE_ROLES;

        if (perm.equalsIgnoreCase("MANAGE_PERMISSIONS"))
            return DisPerm.MANAGE_PERMISSIONS;

        if (perm.equalsIgnoreCase("MANAGE_WEBHOOKS"))
            return DisPerm.MANAGE_WEBHOOKS;

        if (perm.equalsIgnoreCase("MANAGE_EMOTES"))
            return DisPerm.MANAGE_EMOTES;

        if (perm.equalsIgnoreCase("MOD"))
            return DisPerm.MOD;

        if (perm.equalsIgnoreCase("MANAGER"))
            return DisPerm.MOD;

        if (perm.equalsIgnoreCase("USE_SLASH_COMMANDS"))
            return DisPerm.USE_SLASH_COMMANDS;

        throw new PermissionParseException(perm);
    }

    public static DisPerm parse(Permission perm) throws PermissionParseException {
        if (perm == Permission.CREATE_INSTANT_INVITE)
            return DisPerm.CREATE_INSTANT_INVITE;

        if (perm == Permission.KICK_MEMBERS)
            return DisPerm.KICK_MEMBERS;

        if (perm == Permission.BAN_MEMBERS)
            return DisPerm.BAN_MEMBERS;

        if (perm == Permission.ADMINISTRATOR)
            return DisPerm.ADMINISTRATOR;

        if (perm == Permission.MANAGE_CHANNEL)
            return DisPerm.MANAGE_CHANNEL;

        if (perm == Permission.MANAGE_SERVER)
            return DisPerm.MANAGE_SERVER;

        if (perm == Permission.MESSAGE_ADD_REACTION)
            return DisPerm.MESSAGE_ADD_REACTION;

        if (perm == Permission.VIEW_AUDIT_LOGS)
            return DisPerm.VIEW_AUDIT_LOGS;

        if (perm == Permission.PRIORITY_SPEAKER)
            return DisPerm.PRIORITY_SPEAKER;

        if (perm == Permission.VIEW_GUILD_INSIGHTS)
            return DisPerm.VIEW_GUILD_INSIGHTS;

        if (perm == Permission.VIEW_CHANNEL)
            return DisPerm.VIEW_CHANNEL;

        if (perm == Permission.MESSAGE_READ)
            return DisPerm.MESSAGE_READ;

        if (perm == Permission.MESSAGE_WRITE)
            return DisPerm.MESSAGE_WRITE;

        if (perm == Permission.MESSAGE_TTS)
            return DisPerm.MESSAGE_TTS;

        if (perm == Permission.MESSAGE_MANAGE)
            return DisPerm.MESSAGE_MANAGE;

        if (perm == Permission.MESSAGE_EMBED_LINKS)
            return DisPerm.MESSAGE_EMBED_LINKS;

        if (perm == Permission.MESSAGE_ATTACH_FILES)
            return DisPerm.MESSAGE_ATTACH_FILES;

        if (perm == Permission.MESSAGE_HISTORY)
            return DisPerm.MESSAGE_HISTORY;

        if (perm == Permission.MESSAGE_MENTION_EVERYONE)
            return DisPerm.MESSAGE_MENTION_EVERYONE;

        if (perm == Permission.MESSAGE_EXT_EMOJI)
            return DisPerm.MESSAGE_EXT_EMOJI;

        if (perm == Permission.VOICE_STREAM)
            return DisPerm.VOICE_STREAM;

        if (perm == Permission.VOICE_CONNECT)
            return DisPerm.VOICE_CONNECT;

        if (perm == Permission.VOICE_SPEAK)
            return DisPerm.VOICE_SPEAK;

        if (perm == Permission.VOICE_MUTE_OTHERS)
            return DisPerm.VOICE_MUTE_OTHERS;

        if (perm == Permission.VOICE_DEAF_OTHERS)
            return DisPerm.VOICE_DEAF_OTHERS;

        if (perm == Permission.VOICE_MOVE_OTHERS)
            return DisPerm.VOICE_MOVE_OTHERS;

        if (perm == Permission.VOICE_USE_VAD)
            return DisPerm.VOICE_USE_VAD;

        if (perm == Permission.NICKNAME_CHANGE)
            return DisPerm.NICKNAME_CHANGE;

        if (perm == Permission.NICKNAME_MANAGE)
            return DisPerm.NICKNAME_MANAGE;

        if (perm == Permission.MANAGE_ROLES)
            return DisPerm.MANAGE_ROLES;

        if (perm == Permission.MANAGE_PERMISSIONS)
            return DisPerm.MANAGE_PERMISSIONS;

        if (perm == Permission.MANAGE_WEBHOOKS)
            return DisPerm.MANAGE_WEBHOOKS;

        if (perm == Permission.MANAGE_EMOTES)
            return DisPerm.MANAGE_EMOTES;
    
        if (perm == Permission.USE_SLASH_COMMANDS)
            return DisPerm.USE_SLASH_COMMANDS;

        throw new PermissionParseException(perm);
    }

    public static boolean isMod(EnumSet<Permission> enumSet) 
    {
        for (Permission permission : enumSet) {
            if (MOD_PERMS.contains(parse(permission)) || MANAGER_PERMS.contains(parse(permission)))
                return true;
        }
        return false;
    }

    public static boolean isManager(EnumSet<Permission> enumSet) 
    {
        for (Permission permission : enumSet) {
            if (MANAGER_PERMS.contains(parse(permission)))
                return true;
        }
        return false;
    }

    public static List<DisPerm> getAllPerms() {
        List<DisPerm> perms = new ArrayList<DisPerm>();

        perms.add(DisPerm.CREATE_INSTANT_INVITE);
        perms.add(DisPerm.KICK_MEMBERS);
        perms.add(DisPerm.BAN_MEMBERS);
        perms.add(DisPerm.ADMINISTRATOR);
        perms.add(DisPerm.MANAGE_CHANNEL);
        perms.add(DisPerm.MANAGE_SERVER);
        perms.add(DisPerm.MESSAGE_ADD_REACTION);
        perms.add(DisPerm.VIEW_AUDIT_LOGS);
        perms.add(DisPerm.PRIORITY_SPEAKER);
        perms.add(DisPerm.VIEW_GUILD_INSIGHTS);
        perms.add(DisPerm.VIEW_CHANNEL);
        perms.add(DisPerm.MESSAGE_READ);
        perms.add(DisPerm.MESSAGE_WRITE);
        perms.add(DisPerm.MESSAGE_TTS);
        perms.add(DisPerm.MESSAGE_MANAGE);
        perms.add(DisPerm.MESSAGE_EMBED_LINKS);
        perms.add(DisPerm.MESSAGE_ATTACH_FILES);
        perms.add(DisPerm.MESSAGE_HISTORY);
        perms.add(DisPerm.MESSAGE_MENTION_EVERYONE);
        perms.add(DisPerm.MESSAGE_EXT_EMOJI);
        perms.add(DisPerm.VOICE_STREAM);
        perms.add(DisPerm.VOICE_CONNECT);
        perms.add(DisPerm.VOICE_SPEAK);
        perms.add(DisPerm.VOICE_MUTE_OTHERS);
        perms.add(DisPerm.VOICE_DEAF_OTHERS);
        perms.add(DisPerm.VOICE_MOVE_OTHERS);
        perms.add(DisPerm.VOICE_USE_VAD);
        perms.add(DisPerm.NICKNAME_CHANGE);
        perms.add(DisPerm.NICKNAME_MANAGE);
        perms.add(DisPerm.MANAGE_ROLES);
        perms.add(DisPerm.MANAGE_PERMISSIONS);
        perms.add(DisPerm.MANAGE_WEBHOOKS);
        perms.add(DisPerm.MANAGE_EMOTES);
        perms.add(DisPerm.USE_SLASH_COMMANDS);

        return perms;
    }

    public static boolean checkPermission(Member member, DisPerm requiredPermission) 
    {
        EnumSet<Permission> perms = member.getPermissions();
        
        for (Permission p : perms) 
        {
            if (parse(p) == requiredPermission)
                return true;
        }

        if (requiredPermission == DisPerm.MOD) {
            if (isMod(perms))
                return true;
        }
        
        if (requiredPermission == DisPerm.MANAGER) {
            if (isManager(perms))
                return true;
        }

        List<Long> whitelisted = AlloyUtil.getWhitelisted();
        return whitelisted.contains(member.getIdLong());
        // return false;
    }

    public static boolean checkPermission(Member m, DisPerm permission, TextChannel channel) 
    {
        boolean premised = false;
        List<PermissionOverride> overrides = channel.getPermissionOverrides();
        for (PermissionOverride o : overrides) 
        {
            Role role = o.getRole();
            if (m.getRoles().contains(role))
                premised = checkPremised( o , role);
        }
        List<Long> whitelisted = AlloyUtil.getWhitelisted();
        return premised || whitelisted.contains(m.getIdLong());
	}

    private static boolean checkPremised(PermissionOverride o, Role role) 
    {
        boolean b = false;
        for (Permission p : o.getAllowed())
        {
            for (Permission r : role.getPermissions()) 
            {
                if (p == r)
                    b = true;
            }                
        }
        for (Permission p : o.getDenied())
        {
            for (Permission r : role.getPermissions()) 
            {
                if (p == r)
                    b = true;
            }                
        }

        return b;
    }

    public static List<DisPerm> parsePerms(EnumSet<Permission> permissions) 
    {
        List<DisPerm> perms = new ArrayList<DisPerm>();
        for (Permission p : permissions) 
            perms.add(parse(p));
        return perms;
	}
    
} 