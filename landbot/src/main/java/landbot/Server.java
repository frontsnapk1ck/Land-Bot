package landbot;

import landbot.io.Saver;

public class Server {
    
    
    public static final String STARTING_BALANCE = "starting balance";
    public static final String PREFIX = "prefix";
    public static final String COOLDOWN = "cooldown";
    public static final String ROLE_ASSIGN_ON_BUY = "role assign on buy";
    public static final String ADMIN_BYPASS_COOLDOWN = "admin bypass cooldown";

    private String prefix;
    private int startingBalance;
    private int cooldown;
    private String path;
    private boolean roleAssignOnBuy;
    private boolean adminBypassCooldown;

    public Server(String prefix , int startingBalace , int cooldown , String path , boolean roleAssignOnBuy , boolean adminBypassCooldown) 
    {
        this.cooldown = cooldown;
        this.startingBalance = startingBalace;
        this.prefix = prefix;
        this.path = path;
        this.roleAssignOnBuy = roleAssignOnBuy;
        this.adminBypassCooldown = adminBypassCooldown;
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

    public int getCooldown() 
    {
		return cooldown;
	}

    public void changeCooldown(int cooldown) 
    {
        this.cooldown = cooldown;
        saveSettings();
    }
    
    private void saveSettings() 
    {
        String[] out = new String[] {
            PREFIX +                ":" + prefix ,
            STARTING_BALANCE +      ":" + startingBalance ,
            COOLDOWN +              ":" + cooldown ,
            ROLE_ASSIGN_ON_BUY +    ":" + roleAssignOnBuy ,
            ADMIN_BYPASS_COOLDOWN + ":" + adminBypassCooldown 
        };
        Saver.saveOverwite(this.path + "\\settings\\bot.settings", out );
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
}
