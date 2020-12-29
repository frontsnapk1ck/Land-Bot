package alloy.builder;

import java.util.List;

/**
 * t: type of object being loaded f: type of file being loaded
 */
public abstract class DataLoader< T , F >  {

    public T load( F file )
    {
        return null;
    }

    public List<T> loadALl( F file)
    {
        return null;
    }
    
}
