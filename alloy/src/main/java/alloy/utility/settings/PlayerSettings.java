package alloy.utility.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import alloy.gameobjects.Warning;
import alloy.gameobjects.player.Account;
import alloy.gameobjects.player.Building;
import utility.Util;

public class PlayerSettings extends AbstractSettings {
    
    private long id;
    private HashMap<String , List<Building>> owned;
    private List<String> buildingTypes;
    private List<Warning> warnings;
    private Account account;
    private int xp;
    
    public PlayerSettings() 
    {
        this.id = 0l;
        this.owned = new HashMap<String , List<Building>>();
        this.buildingTypes = new ArrayList<String>();
        this.warnings = new ArrayList<Warning>();
        this.account = null;
        this.xp = 0;
        setPath( "" );
    }

    public Account getAccount() 
    {
        return account;
    }

    public List<String> getBuildingTypes() 
    {
        return buildingTypes;
    }

    public long getId() 
    {
        return id;
    }

    public HashMap<String, List<Building>> getOwned() 
    {
        return owned;
    }


    public List<Warning> getWarnings() 
    {
        return warnings;
    }

    public int getXp() 
    {
        return xp;
    }

    public PlayerSettings setAccount(Account account) 
    {
        this.account = account;
        return this;
    }

    public PlayerSettings setBuildingTypes(List<String> buildingTypes) 
    {
        Util.copy(this.buildingTypes, buildingTypes);
        return this;
    }

    public PlayerSettings setId(long id) 
    {
        this.id = id;
        return this;
    }

    public PlayerSettings setOwned(HashMap<String, List<Building>> owned) 
    {
        Util.copy(this.owned , owned);
        return this;
    }

    public PlayerSettings setPath(String path) 
    {
        super.setPath(path);
        return this;
    }

    public PlayerSettings setWanrings(List<Warning> warnings) 
    {
        Util.copy(this.warnings , warnings);
        return this;
    }

    public PlayerSettings addWarnings(List<Warning> warnings) 
    {
        List<Warning> tmp = new ArrayList<Warning>();
        Util.copy(tmp , warnings);
        this.warnings.addAll(tmp);
        return this;
    }

    public PlayerSettings addWarning(Warning warning) 
    {
        this.warnings.add(warning);
        return this;
    }

    public PlayerSettings removeWarnings(List<Warning> warnings) 
    {
        List<Warning> tmp = new ArrayList<Warning>();
        Util.copy(tmp , warnings);
        this.warnings.removeAll(tmp);
        return this;
    }

    public PlayerSettings removeWarning(Warning warning) 
    {
        this.warnings.remove(warning);
        return this;
    }
    

    public PlayerSettings setXp(int xp) 
    {
        this.xp = xp;
        return this;
    }

    public PlayerSettings copy ()
    {
        PlayerSettings settings = new PlayerSettings();
        settings.setAccount(account)
                .setBuildingTypes(buildingTypes)
                .setId(id)
                .setOwned(owned)
                .setPath(getPath())
                .setWanrings(warnings)
                .setXp(xp);
            
        return settings;
    }

}
