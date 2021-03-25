package frontsnapk1ck.alloy.utility.type;

public class MapNode<T,K> {

    private T key;
    private K value;

    public MapNode(T key , K value) 
    {
        this.key = key;
        this.value = value;
    }
    
    public T getKey() 
    {
        return key;
    }

    public K getValue() 
    {
        return value;
    }
}
