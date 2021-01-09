package alloy.utility.type;

public class MapNode<T,K> {

    private T arg0;
    private K arg1;

    public MapNode(T arg0 , K arg1) 
    {
        this.arg0 = arg0;
        this.arg1 = arg1;
    }
    
    public T getArg0() {
        return arg0;
    }

    public K getArg1() {
        return arg1;
    }
}
