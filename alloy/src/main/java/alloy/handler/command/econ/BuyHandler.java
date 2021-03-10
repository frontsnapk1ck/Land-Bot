package alloy.handler.command.econ;

import java.util.List;

import alloy.gameobjects.player.Building;

public class BuyHandler {

    public static boolean validBuildingName(String name, List<Building> buildings) 
    {
        for (Building b : buildings) 
        {
            if (b.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
	}

    public static Building getToBuy(String name, List<Building> buildings) 
    {
        for (Building b : buildings) 
        {
            if (b.getName().equalsIgnoreCase(name))
                return b;
        }
        return null;
	}
    
}
