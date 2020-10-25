package landbot.builder;

import java.util.List;

/**
 * t: type of object being loaded f: type of file being loaded
 */
public abstract class DataLoader< T , F >  {

    public abstract T load( F file );

    public List<T> loadALl( F file)
    {
        return null;
    }
    
}
