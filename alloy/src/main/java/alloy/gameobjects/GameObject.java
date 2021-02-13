package alloy.gameobjects;

import utility.cache.Cacheable;

public abstract class GameObject implements Cacheable<GameObject> {

    protected abstract void save();

    public abstract GameObject copy();

    @Override
    public long getKeepTime() 
    {
        return DEFAULT_TIME;
    }
    
}
