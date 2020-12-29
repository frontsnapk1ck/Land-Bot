package alloy.gameobjects.player;

import alloy.gameobjects.GameObject;
import alloy.utility.settings.BuildingSettings;

public class Building extends GameObject implements Comparable<Building> {
    
    private BuildingSettings settings;

    public Building(BuildingSettings settings)
    {
        this.settings = settings;
    }
    
    public int getCost() {
        return this.settings.getCost();
    }
    
    public int getGeneration() {
        return this.settings.getGeneration();
    }
    
    public String getName() {
        return this.settings.getName();
    }

    @Override
    public String toString() 
    {
        return  this.getName() + ":" +
                this.getCost() + ":" + 
                this.getGeneration();
    }

    @Override
    public int compareTo(Building o) 
    {
        if (o.getCost() > this.getCost())
            return -1;
        else if (o.getCost() < this.getCost())
            return 1;
        return 0;
    }

    public Building copy() 
    {
		return new Building ( this.settings.copy() );
	}

    /**
     * Cannot save buildings directly
     */
    @Override
    protected void save() {}
}
