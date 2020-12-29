package alloy.utility.discord.perm;

public class DisPerm {
    
    public static final DisPerm CREATE_INSTANT_INVITE       = new DisPerm(  "CREATE_INSTANT_INVITE"     );
    public static final DisPerm KICK_MEMBERS                = new DisPerm(  "KICK_MEMBERS"              );
    public static final DisPerm BAN_MEMBERS                 = new DisPerm(  "BAN_MEMBERS"               );
    public static final DisPerm ADMINISTRATOR               = new DisPerm(  "ADMINISTRATOR"             );
    public static final DisPerm MANAGE_CHANNEL              = new DisPerm(  "MANAGE_CHANNEL"            );
    public static final DisPerm MANAGE_SERVER               = new DisPerm(  "MANAGE_SERVER"             );
    public static final DisPerm MESSAGE_ADD_REACTION        = new DisPerm(  "MESSAGE_ADD_REACTION"      );
    public static final DisPerm VIEW_AUDIT_LOGS             = new DisPerm(  "VIEW_AUDIT_LOGS"           );
    public static final DisPerm PRIORITY_SPEAKER            = new DisPerm(  "PRIORITY_SPEAKER"          );
    public static final DisPerm VIEW_GUILD_INSIGHTS         = new DisPerm(  "VIEW_GUILD_INSIGHTS"       );
    public static final DisPerm VIEW_CHANNEL                = new DisPerm(  "VIEW_CHANNEL"              );
    public static final DisPerm MESSAGE_READ                = new DisPerm(  "MESSAGE_READ"              );
    public static final DisPerm MESSAGE_WRITE               = new DisPerm(  "MESSAGE_WRITE"             );
    public static final DisPerm MESSAGE_TTS                 = new DisPerm(  "MESSAGE_TTS"               );
    public static final DisPerm MESSAGE_MANAGE              = new DisPerm(  "MESSAGE_MANAGE"            );
    public static final DisPerm MESSAGE_EMBED_LINKS         = new DisPerm(  "MESSAGE_EMBED_LINKS"       );
    public static final DisPerm MESSAGE_ATTACH_FILES        = new DisPerm(  "MESSAGE_ATTACH_FILES"      );
    public static final DisPerm MESSAGE_HISTORY             = new DisPerm(  "MESSAGE_HISTORY"           );
    public static final DisPerm MESSAGE_MENTION_EVERYONE    = new DisPerm(  "MESSAGE_MENTION_EVERYONE"  );
    public static final DisPerm MESSAGE_EXT_EMOJI           = new DisPerm(  "MESSAGE_EXT_EMOJI"         );
    public static final DisPerm VOICE_STREAM                = new DisPerm(  "VOICE_STREAM"              );
    public static final DisPerm VOICE_CONNECT               = new DisPerm(  "VOICE_CONNECT"             );
    public static final DisPerm VOICE_SPEAK                 = new DisPerm(  "VOICE_SPEAK"               );
    public static final DisPerm VOICE_MUTE_OTHERS           = new DisPerm(  "VOICE_MUTE_OTHERS"         );
    public static final DisPerm VOICE_DEAF_OTHERS           = new DisPerm(  "VOICE_DEAF_OTHERS"         );
    public static final DisPerm VOICE_MOVE_OTHERS           = new DisPerm(  "VOICE_MOVE_OTHERS"         );
    public static final DisPerm VOICE_USE_VAD               = new DisPerm(  "VOICE_USE_VAD"             );
    public static final DisPerm NICKNAME_CHANGE             = new DisPerm(  "NICKNAME_CHANGE"           );
    public static final DisPerm NICKNAME_MANAGE             = new DisPerm(  "NICKNAME_MANAGE"           );
    public static final DisPerm MANAGE_ROLES                = new DisPerm(  "MANAGE_ROLES"              );
    public static final DisPerm MANAGE_PERMISSIONS          = new DisPerm(  "MANAGE_PERMISSIONS"        );
    public static final DisPerm MANAGE_WEBHOOKS             = new DisPerm(  "MANAGE_WEBHOOKS"           );
    public static final DisPerm MANAGE_EMOTES               = new DisPerm(  "MANAGE_EMOTES"             );

    public static final DisPerm MOD                         = new DisPerm(  "MOD"                       );


    private String name;

    protected DisPerm( String name )
    {
        this.name = name;
    }
  
    public String getName() 
    {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (obj instanceof DisPerm )
        {
            DisPerm perm = (DisPerm) obj;
            return perm.getName().equalsIgnoreCase(this.getName());
        }
        return false;
    }

}

