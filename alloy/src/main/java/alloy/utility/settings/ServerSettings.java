package alloy.utility.settings;

import java.util.ArrayList;
import java.util.List;

import alloy.gameobjects.RankUp;
import utility.Util;

public class ServerSettings extends AbstractSettings {

    private String prefix;
    private int startingBalance;
    private int cooldown;
    private boolean roleAssignOnBuy;
    private boolean adminBypassCooldown;
    private long spamChannel;
    private List<Long> blacklistedChannels;
    private int xpCooldown;
    private List<RankUp> rankups;
    private long id;
    private long modLogChannel;
    private Long userLogChannel;
    private long muteRole;
    private String banAppealLink;


    public ServerSettings() 
    {
        this.cooldown = 10;
        this.startingBalance = 1000;
        this.prefix = "!";
        setPath( "" );
        this.roleAssignOnBuy = false;
        this.adminBypassCooldown = false;
        this.spamChannel = 0l;
        this.blacklistedChannels = new ArrayList<Long>();
        this.xpCooldown = 4;
        this.rankups = new ArrayList<RankUp>();
        this.id = 0l;
        this.modLogChannel = 0l;
        this.userLogChannel = 0l;
        this.muteRole = 0l;
        this.banAppealLink = "none";
    }

    public List<Long> getBlacklistedChannels() 
    {
        return blacklistedChannels;
    }

    public int getCooldown() {
        return cooldown;
    }
    
    public long getId() {
        return id;
    }

    public long getModLogChannel() {
        return modLogChannel;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<RankUp> getRankups() {
        return rankups;
    }

    public long getSpamChannel() {
        return spamChannel;
    }

    public int getStartingBalance() {
        return startingBalance;
    }

    public Long getUserLogChannel() {
        return userLogChannel;
    }

    public int getXpCooldown() {
        return xpCooldown;
    }

    public long getMuteRoleID() 
    {
		return this.muteRole;
    }

    public String getBanAppealLink() {
        return banAppealLink;
    }

    public ServerSettings setBanAppealLink(String banAppealLink) 
    {
        this.banAppealLink = banAppealLink;
        return this;
    }
    
    public ServerSettings setMuteRole(long muteRole) 
    {
        this.muteRole = muteRole;
        return this;
    }

    public ServerSettings setAdminBypassCooldown(boolean adminBypassCooldown) 
    {
        this.adminBypassCooldown = adminBypassCooldown;
        return this;
    }

    public ServerSettings setBlacklistedChannels(List<Long> blacklistedChannels) 
    {
        Util.copy(this.blacklistedChannels, blacklistedChannels);
        return this;
    }

    public ServerSettings addBlacklistedChannels(List<Long> blacklist) 
    { 
    	for (Long channel : blacklist)
            this.blacklistedChannels.add( channel );
        
        return this;
    }

    public ServerSettings addBlacklistedChannel( Long blacklist ) 
    { 
        this.blacklistedChannels.add( blacklist );
        return this;
    }

    public boolean removeBlackListedChannel( List<Long> blacklist ) 
    {
        return this.blacklistedChannels.removeAll(blacklist);
    }
    
    public boolean removeBlackListedChannel(long l) 
    {
        return this.blacklistedChannels.remove(l);
	}

    public ServerSettings setCooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public ServerSettings setId(long id) 
    {
        this.id = id;
        return this;
    }

    public ServerSettings setModLogChannel(long modLogChannel) 
    {
        this.modLogChannel = modLogChannel;
        return this;
    }

    public ServerSettings setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public ServerSettings setRankups(List<RankUp> rankups) 
    {
        Util.copy(this.rankups,  rankups);
        return this;
    }
    
    public ServerSettings addRankUps( List<RankUp> rankups ) 
    { 
    	for (RankUp rank : rankups)
            this.rankups.add( rank );

        return this;
    }

    public ServerSettings addRankUp( RankUp rankup ) 
    { 
        this.rankups.add( rankup );
        return this;
    }

    public boolean removeRankUp(List<RankUp> rankUps) 
    {
		return this.rankups.removeAll(rankUps);
	}

    public boolean removeRankUp(RankUp r) 
    {
		return this.rankups.remove(r);
	}

    public ServerSettings setRoleAssignOnBuy(boolean roleAssignOnBuy) 
    {
        this.roleAssignOnBuy = roleAssignOnBuy;
        return this;

    }

    public ServerSettings setSpamChannel(long spamChannel) 
    {
        this.spamChannel = spamChannel;
        return this;
    }

    public ServerSettings setStartingBalance(int startingBalance) 
    {
        this.startingBalance = startingBalance;
        return this;
    }

    public ServerSettings setUserLogChannel(Long userLogChannel) 
    {
        this.userLogChannel = userLogChannel;
        return this;
    }

    public ServerSettings setXpCooldown(int xpCooldown) 
    {
        this.xpCooldown = xpCooldown;
        return this;
    }

    @Override
    public ServerSettings setPath(String path) 
    {
        super.setPath(path);
        return this;
    }
 
    public boolean isAdminBypassCooldown() {
        return adminBypassCooldown;
    }

    public boolean isRoleAssignOnBuy() {
        return roleAssignOnBuy;
    }

    public ServerSettings copy()
    {
        ServerSettings settings = new ServerSettings();
        settings.setAdminBypassCooldown(adminBypassCooldown)
                .setBlacklistedChannels(blacklistedChannels)
                .setCooldown(cooldown)
                .setId(id)
                .setModLogChannel(modLogChannel)
                .setMuteRole(muteRole)
                .setPath(getPath())
                .setPrefix(prefix)
                .setRankups(rankups)
                .setRoleAssignOnBuy(roleAssignOnBuy)
                .setSpamChannel(spamChannel)
                .setStartingBalance(startingBalance)
                .setUserLogChannel(userLogChannel)
                .setXpCooldown(xpCooldown)
                .setBanAppealLink(banAppealLink);
        
        return settings;
    }

}
