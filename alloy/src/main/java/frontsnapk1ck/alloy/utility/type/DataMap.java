package frontsnapk1ck.alloy.utility.type;

import java.util.ArrayList;
import java.util.List;

public class DataMap<T,K> {

    private List<MapNode<T,K>> nodes;

    public DataMap() 
    {
        nodes = new ArrayList<MapNode<T,K>>();
    }

    public List<MapNode<T, K>> getNodes() {
        return nodes;
    }

    public K get(T key)
    {
        for (MapNode<T,K> mapNode : nodes) 
        {
            if (mapNode.getArg0().equals(key))
                return mapNode.getArg1();
        }
        return null;
    }
    
}
