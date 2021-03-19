package frontsnapk1ck.alloy.utility.settings;

public class BuildingSettings {
    
    private int cost;
    private int generation;
    private String name;

    public BuildingSettings() 
    {
        this.cost = 0;
        this.generation = 0;
        this.name = "NO NAME";
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

    public BuildingSettings setCost(int cost) 
    {
        this.cost = cost;
        return this;
    }

    public BuildingSettings setGeneration(int generation) 
    {
        this.generation = generation;
        return this;
    }

    public BuildingSettings setName(String name) 
    {
        this.name = name;
        return this;
    }

    public BuildingSettings copy()
    {
        BuildingSettings settings = new BuildingSettings();
        settings.setCost(cost)
                .setGeneration(generation)
                .setName(name);
        
        return settings;
    }

}
