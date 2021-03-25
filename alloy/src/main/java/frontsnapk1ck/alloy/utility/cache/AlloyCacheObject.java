package frontsnapk1ck.alloy.utility.cache;

import frontsnapk1ck.utility.cache.CacheObject;
import frontsnapk1ck.utility.cache.Cacheable;

public class AlloyCacheObject extends CacheObject<Cacheable> {

    private int accessed;

    public AlloyCacheObject(Cacheable value, long keepTime) 
    {
        super(value, keepTime);
        this.accessed = 0;
    }
    @Override
    public Cacheable getValue() 
    {
        this.accessed ++;
        return super.getValue();
    }

    public int getAccessed() 
    {
        return accessed;
    }
    
}
