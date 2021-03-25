package frontsnapk1ck.alloy.gameobjects;

public class ServerNew {
    private String prefix;
    private long startingBalance;
    private long cooldown;
    private long xpCooldown;
    private boolean roleAssign;
    private Channels channels;
    private long id;
    private Roles roles;

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String value) 
    {
        this.prefix = value;
    }

    public long getStartingBalance() 
    {
        return startingBalance;
    }

    public void setStartingBalance(long value) 
    {
        this.startingBalance = value;
    }

    public long getCooldown() 
    {
        return cooldown;
    }

    public void setCooldown(long value) 
    {
        this.cooldown = value;
    }

    public long getXPCooldown() 
    {
        return xpCooldown;
    }

    public void setXPCooldown(long value) 
    {
        this.xpCooldown = value;
    }

    public boolean getRoleAssign() 
    {
        return roleAssign;
    }

    public void setRoleAssign(boolean value) 
    {
        this.roleAssign = value;
    }

    public Channels getChannels() 
    {
        return channels;
    }

    public void setChannels(Channels value) 
    {
        this.channels = value;
    }

    public long getID() 
    {
        return id;
    }

    public void setID(long value) 
    {
        this.id = value;
    }

    public Roles getRoles() 
    {
        return roles;
    }

    public void setRoles(Roles value) 
    {
        this.roles = value;
    }

    public class Channels 
    {

        private long spam;
        private long[] blacklisted;
        private long mod;
        private long user;

        public Object getSpam() 
        {
            return spam;
        }

        public void setSpam(long value) 
        {
            this.spam = value;
        }

        public long[] getBlacklisted() 
        {
            return blacklisted;
        }

        public void setBlacklisted(long[] value) 
        {
            this.blacklisted = value;
        }

        public long getMod() 
        {
            return mod;
        }

        public void setMod(long value) 
        {
            this.mod = value;
        }

        public long getUser() 
        {
            return user;
        }

        public void setUser(long value) 
        {
            this.user = value;
        }
    }

    public class Roles{

        private String mute;
        private Building[] building;

        public String getMute() 
        {
            return mute;
        }

        public void setMute(String value) 
        {
            this.mute = value;
        }

        public Building[] getBuilding() 
        {
            return building;
        }

        public void setBuilding(Building[] value) 
        {
            this.building = value;
        }
    }

    public class Building {

        private String name;
        private long id;

        public String getName() 
        {
            return name;
        }

        public void setName(String value) 
        {
            this.name = value;
        }

        public long getID() 
        {
            return id;
        }

        public void setID(long value) 
        {
            this.id = value;
        }
    }
}
