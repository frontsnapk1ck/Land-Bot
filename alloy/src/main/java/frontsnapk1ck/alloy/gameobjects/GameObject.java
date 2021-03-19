package frontsnapk1ck.alloy.gameobjects;

import frontsnapk1ck.utility.cache.Cacheable;

public abstract class GameObject implements Cacheable<GameObject> {

    protected abstract void save();

    public abstract GameObject copy();

    
}
