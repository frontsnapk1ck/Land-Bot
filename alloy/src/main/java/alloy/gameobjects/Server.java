package alloy.gameobjects;

import java.util.List;

import alloy.utility.settings.ServerSettings;
import io.Saver;

public class Server extends GameObject {
    
    
    public static final String STARTING_BALANCE = "starting balance";
    public static final String PREFIX = "prefix";
    public static final String COOLDOWN = "cooldown";
    public static final String ROLE_ASSIGN_ON_BUY = "role assign on buy";
    public static final String ADMIN_BYPASS_COOLDOWN = "admin bypass cooldown";
    public static final String SPAM_CHANNEL = "spam channel";
    public static final String BLACKLISTED_CHANNELS = "blacklisted";
    public static final String XP_COOLDOWN = "xp cooldown";
    public static final String RANKUPS = "rankups";
    public static final String MOD_LOG_CHANNEL = "mod log channel";
    public static final String USER_LOG_CHANNEL = "user log channel";
    public static final String ID = "id";
    public static final String MUTE_ROLE_ID = "mute roll";
    public static final String BAN_APPEAL_LINK = "appeal link";

    private ServerSettings settings;

    public Server(ServerSettings settings)
    {
        this.settings = settings;
    }

    public void changePrefix(String p)
    {
        settings.setPrefix(p);
        save();
    }

    public void changeStartingBalance ( int bal )
    {
        settings.setStartingBalance(bal);
        save();
    }

    public String getPrefix() 
    {
        return settings.getPrefix();
    }
    
    public int getStartingBalance() 
    {
        return settings.getStartingBalance();
    }

    public int getWorkCooldown() 
    {
		return settings.getCooldown();
    }
    
    public int getXPCooldown()
    {
        return settings.getXpCooldown();
    }

    public void changeCooldown(int cooldown) 
    {
        settings.setCooldown(cooldown);
        save();
    }
    
    @Override
    protected void save() 
    {
        String blacklistedChannels = getBlacklistedChannelsString();
        String[] rankups = getRankupsSave();
        String[] out = new String[] 
        {
            PREFIX +                ":" + this.settings.getPrefix() ,
            STARTING_BALANCE +      ":" + this.settings.getStartingBalance() ,
            COOLDOWN +              ":" + this.settings.getCooldown() ,
            ROLE_ASSIGN_ON_BUY +    ":" + this.settings.isRoleAssignOnBuy() ,
            ADMIN_BYPASS_COOLDOWN + ":" + this.settings.isAdminBypassCooldown() ,
            SPAM_CHANNEL +          ":" + this.settings.getSpamChannel() , 
            BLACKLISTED_CHANNELS + ":" + blacklistedChannels , 
            XP_COOLDOWN +           ":" + this.settings.getXpCooldown() , 
            ID +                    ":" + this.settings.getId() , 
            MOD_LOG_CHANNEL +       ":" + this.settings.getModLogChannel(),
            USER_LOG_CHANNEL +      ":" + this.settings.getUserLogChannel() ,
            BAN_APPEAL_LINK +       ":" + this.settings.getBanAppealLink() ,

        };
        Saver.saveOverwrite(this.settings.getPath() + "\\settings\\bot.settings", out );
        Saver.saveOverwrite(this.settings.getPath() + "\\settings\\rank.ups", rankups );
        
    }

    private String[] getRankupsSave() 
    {
        String[] out = new String[this.settings.getRankups().size()];

        int i = 0;
        for (RankUp r : this.settings.getRankups()) 
        {
            out[i] = r.toSave();
            i++;    
        }

        return out;
    }

    private String getBlacklistedChannelsString() 
    {
        String out = "";
        for (Long l : this.settings.getBlacklistedChannels()) 
            out += "" + l + ";";
        
        return out;
    }

    public String getPath() {
        return this.settings.getPath();
    }

    public boolean getRoleAssignOnBuy() 
    {
		return this.settings.isRoleAssignOnBuy();
	}

    public void changeAssignRolesOnBuy(boolean b) 
    {
        this.settings.setRoleAssignOnBuy(b);
        this.save();
	}

    public boolean getAdminCooldownBypass() 
    {
		return this.settings.isAdminBypassCooldown();
    }
    
    public void changeAdminCooldownBypass(boolean b) 
    {
        this.settings.setAdminBypassCooldown(b);
        this.save();
    }

    public long getSpamChannel() 
    {
        return this.settings.getSpamChannel();
    }

    public long getModLogChannel() 
    {
        return this.settings.getModLogChannel();
    }

    public Long getUserLogChannel() 
    {
        return this.settings.getUserLogChannel();
    }

    public void changeSpamChannel(long spamChannel) 
    {
        this.settings.setSpamChannel(spamChannel);
        this.save();
    }

    public List<Long> getBlacklistedChannels() 
    {
		return this.settings.getBlacklistedChannels();
    }

    public boolean removeBlackListedChannel( long l )
    {
        boolean b = this.settings.removeBlackListedChannel( l );
        this.save();
        return b;
    }
    
    public void addBlacklistedChanel( long l )
    {
        this.settings.addBlacklistedChannel(l);
        this.save();
    }
    
    public void changeXPCooldown(int time)
    {
        this.settings.setXpCooldown(time);
        this.save();
    }

    public List<RankUp> getRankups() 
    {
        return this.settings.getRankups();
    }

    public boolean removeRankUp(RankUp r)
    {
        boolean b = this.settings.removeRankUp(r);
        this.save();
        return b;
    }

    public void addRankUp(RankUp r)
    {
        this.settings.addRankUp(r);
        this.save();
    }

    public long getId() 
    {
        return this.settings.getId();
    }

    public long getMuteRollID() 
    {
		return settings.getMuteRoleID();
	}

    @Override
    public Server copy() 
    {
        return new Server ( this.settings.copy() );
    }

    @Override
    public GameObject getData() {
        return this;
    }

    public void setBanAppealLink(String string) 
    {
        this.settings.setBanAppealLink(string);
        this.save();
	}

    public void setMuteRole(long idLong) 
    {
        this.settings.setMuteRole(idLong);
        this.save();
	}

    public void setModLog(long idLong) 
    {
        this.settings.setModLogChannel(idLong);
        this.save();
	}

    public String getBanAppealLink() 
    {
		return this.settings.getBanAppealLink();
	}
}
