package alloy.gameobjects.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import alloy.gameobjects.GameObject;
import alloy.gameobjects.Warning;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.settings.PlayerSettings;
import io.Saver;

public class Player extends GameObject{

    private PlayerSettings settings;

    public Player (PlayerSettings settings )
    {
        this.settings = settings;
    }

    public HashMap<String, List<Building>> getOwned() 
    {
        return this.settings.getOwned();
    }

    public List<Building> getOwned(String s) 
    {
        if (this.settings.getOwned().containsKey(s))
            this.settings.getOwned().get(s);
        return null;
    }

    public long getId() 
    {
        return this.settings.getId();
    }

    public int getNumType(Building b) 
    {
		return this.settings.getOwned().get(b.getName()).size();
	}

    public int getTotalOwned() 
    {
        int i = 0;
        for (String string : this.settings.getBuildingTypes()) 
        {
            i += this.settings.getOwned().get(string).size();
        }
        return i;
	}

    public boolean buyBuilding(Building b) 
    {
        int cost = b.getCost();
        String key = b.getName();

        if (this.settings.getAccount().subtract(cost))
        {
            this.settings.getOwned().get(key).add(b);
            this.save();
            return true;
        }
        return false;

	}

    public int getBal() 
    {
		return this.settings.getAccount().getBal();
	}

    public void addBal(int bal) 
    {
        this.settings.getAccount().add(bal);
        this.save();
	}

    public List<Building> getListOwned() 
    {
        List<Building> tmp = new ArrayList<Building>();

        for (String s : this.settings.getBuildingTypes()) 
        {
            if (this.settings.getOwned().get(s).size() != 0)
                tmp.addAll(this.settings.getOwned().get(s));
        }

        return tmp;
    }

    public List<String> getTypes() 
    {
		return this.settings.getBuildingTypes();
	}

    public void removeBuilding(Building b) 
    {
        String key = b.getName();
        if (this.settings.getOwned().get(key) != null)
        {
            int numOwned = this.settings.getOwned().get(key).size();
            int refund = numOwned * b.getCost();
            this.settings.getOwned().remove(key);
            this.settings.getBuildingTypes().remove(key);
            this.addBal(refund);
        }
	}

    public void day() 
    {
        int bal = 0;
        for (String string : this.settings.getBuildingTypes()) 
        {
            List<Building> b = this.settings.getOwned().get(string);
            if (b != null && b.size() > 0)
            {
                int gen = b.get(0).getGeneration();
                int tmp = b.size() * gen;
                bal += tmp;
            }  
        }
        this.addBal(bal);
    }
    
    public boolean canSpend(int amt)
    {
        return this.settings.getAccount().getBal() >= amt;
    }

    public void spend(int amt)
    {
        this.settings.getAccount().subtract(amt);
        this.save();
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (obj instanceof Player)
        {
            Player p = (Player)obj;
            return p.getId() == this.settings.getId();
        }
        return false;
    }

    @Override
    protected void save() 
    {
        this.settings.getAccount().save();
        
        saveBuildings();
        saveXP();
                
    }

    private void saveXP() 
    {
        String path = this.settings.getPath() + "\\rank.txt";
        String[] out = new String[1];
        out[0] = "" + this.settings.getXp();

        Saver.saveOverwrite(path, out);
    }

    private void saveBuildings() 
    {
        String buildingPath = this.settings.getPath() + AlloyUtil.SUB + AlloyUtil.BUILDING_FILE;

        int size = this.settings.getBuildingTypes().size();
        String[] out = new String[size];
        
        int i = 0;
        for (String s : this.settings.getBuildingTypes()) 
        {
            String tmp = "";
            tmp += s;
            tmp += ">";
            tmp += this.settings.getOwned().get(s).size();

            out[i] = tmp;
            i++;
        }

        Saver.saveOverwrite(buildingPath, out);
    }

    public int getXP() 
    {
		return this.settings.getXp();
	}

    public Player copy() 
    {
		return new Player( this.settings );
	}

    public void addXP(int xp) 
    {
        int newXP = this.settings.getXp() + xp;
        this.settings.setXp(newXP);
        this.save();
	}

    public void setXP(int xp) 
    {
        this.settings.setXp(xp);
        this.save();
    }

    public void addWarning(Warning w) 
    {
        this.settings.addWarning(w);
	}


    public void removeWarning(Warning w) 
    {
        this.settings.removeWarning(w);
        String path = this.settings.getPath() + 
                        AlloyUtil.WARNINGS_FOLDER + 
                        AlloyUtil.SUB + w.getId() + 
                        AlloyUtil.WARNING_EXT;
        Saver.deleteFile(path);
    }
    
    public String getAsMention()
    {
        return "<@!" + this.getId() + ">";
    }

    public String getPath()
    {
        return settings.getPath();
    }

    @Override
    public Player getData() 
    {
        return this;
    }

}
