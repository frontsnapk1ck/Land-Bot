package landbot.gameobjects;

import java.util.List;

import landbot.io.Saver;

public class Server {
    
    
    public static final String STARTING_BALANCE = "starting balance";
    public static final String PREFIX = "prefix";
    public static final String COOLDOWN = "cooldown";
    public static final String ROLE_ASSIGN_ON_BUY = "role assign on buy";
    public static final String ADMIN_BYPASS_COOLDOWN = "admin bypass cooldown";
    public static final String SPAM_CHANNEL = "spam channel";
    public static final String BALCKLISTED_CHANNENLS = "blacklisted";
    public static final String XP_COOLDOWN = "xp cooldown";
    public static final String RANKUPS = "rankups";

    private String prefix;
    private int startingBalance;
    private int cooldown;
    private String path;
    private boolean roleAssignOnBuy;
    private boolean adminBypassCooldown;
    private long spamChannel;
    private List<Long> blacklistedChannels;
    private int xpCooldown;
    private List<RankUp> rankups;

    public Server(  String prefix , int startingBalace , int cooldown , String path , 
                    boolean roleAssignOnBuy , boolean adminBypassCooldown , long spamChannel , List<Long> blacklist , int xpCooldown , List<RankUp> rankups )     {
        this.cooldown = cooldown;
        this.startingBalance = startingBalace;
        this.prefix = prefix;
        this.path = path;
        this.roleAssignOnBuy = roleAssignOnBuy;
        this.adminBypassCooldown = adminBypassCooldown;
        this.spamChannel = spamChannel;
        this.blacklistedChannels = blacklist;
        this.xpCooldown = xpCooldown;
        this.rankups = rankups;
    }

    public void changePrefix(String p)
    {
        prefix = p;
        saveSettings();
    }

    public void changeStartingBalance ( int bal )
    {
        startingBalance = bal;
        saveSettings();
    }

    public String getPrefix() {
        return prefix;
    }
    
    public int getStartingBalance() {
        return startingBalance;
    }

    public int getWorkCooldown() 
    {
		return cooldown;
    }
    
    public int getXPCooldown()
    {
        return this.xpCooldown;
    }

    public void changeCooldown(int cooldown) 
    {
        this.cooldown = cooldown;
        saveSettings();
    }
    
    private void saveSettings() 
    {
        String blacklistedChannels = getBlacklistedChannelsString();
        String[] rankups = getRankupsSave();
        String[] out = new String[] 
        {
            PREFIX +                ":" + prefix ,
            STARTING_BALANCE +      ":" + startingBalance ,
            COOLDOWN +              ":" + cooldown ,
            ROLE_ASSIGN_ON_BUY +    ":" + roleAssignOnBuy ,
            ADMIN_BYPASS_COOLDOWN + ":" + adminBypassCooldown ,
            SPAM_CHANNEL +          ":" + spamChannel , 
            BALCKLISTED_CHANNENLS + ":" + blacklistedChannels , 
            XP_COOLDOWN +           ":" + xpCooldown

        };
        Saver.saveOverwite(this.path + "\\settings\\bot.settings", out );
        Saver.saveOverwite(this.path + "\\settings\\rank.ups", rankups );
        
    }

    private String[] getRankupsSave() 
    {
        String[] out = new String[this.rankups.size()];

        int i = 0;
        for (RankUp r : this.rankups) 
        {
            out[i] = r.toSave();
            i++;    
        }

        return out;
    }

    private String getBlacklistedChannelsString() 
    {
        String out = "";
        for (Long l : blacklistedChannels) 
            out += "" + l + ";";
        
        return out;
    }

    public String getPath() {
        return path;
    }

    public boolean getRoleAssignOnBuy() 
    {
		return this.roleAssignOnBuy;
	}

    public void changeAssignRolesOnBuy(boolean b) 
    {
        this.roleAssignOnBuy = b;
        this.saveSettings();
	}

    public boolean getAdminCooldownBypass() 
    {
		return this.adminBypassCooldown;
    }
    
    public void changeAdminCooldownBypass(boolean b) 
    {
        this.adminBypassCooldown = b;
        this.saveSettings();
    }

    public long getSpamChannel() 
    {
        return spamChannel;
    }

    public void changeSpamChannel(long spamChannel) 
    {
        this.spamChannel = spamChannel;
        this.saveSettings();
    }

    public List<Long> getBlacklistedChannels() 
    {
		return this.blacklistedChannels;
    }

    public boolean removeBlackListedChannel( long l )
    {
        boolean b = this.blacklistedChannels.remove(l);
        this.saveSettings();
        return b;
    }
    
    public void addBlacklistedChanel( long l )
    {
        this.blacklistedChannels.add(l);
        this.saveSettings();
    }
    
    public void changeXPCooldown(int time)
    {
        this.xpCooldown = time;
        this.saveSettings();
    }

    public List<RankUp> getRankups() 
    {
        return rankups;
    }

    public boolean removeRankUp(RankUp r)
    {
        boolean b = this.rankups.remove(r);
        this.saveSettings();
        return b;
    }

    public void addRankUp(RankUp r)
    {
        this.rankups.add(r);
        this.saveSettings();
    }
}
