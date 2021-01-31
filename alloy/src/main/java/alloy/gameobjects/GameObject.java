package alloy.gameobjects;

import alloy.gameobjects.cache.Cacheable;

public abstract class GameObject implements Cacheable<GameObject> {

    protected abstract void save();

    public abstract GameObject copy();
    
}
