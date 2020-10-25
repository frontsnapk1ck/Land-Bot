package landbot.player;

import landbot.utility.GameObject;

public class Building extends GameObject implements Comparable<Building> {

    private int cost;
    private int generation;
    private String name;

    public Building ( String name , int cost , int generation )
    {
        this.name = name;
        this.cost = cost;
        this.generation = generation;
    }
    
    
    public int getCost() {
        return cost;
    }
    
    public int getGeneration() {
        return generation;
    }
    
    public String getName() {
        return name;
    }

    @Override
    public String toString() 
    {
        return  this.name + ":" +
                this.cost + ":" + 
                this.generation;
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

	public Building copy() {
		return new Building ( this.name , this.cost , this.generation );
	}

    /**
     * Cannot save buildings directly
     */
    @Override
    protected void save() {}
}
