package frontsnapk1ck.utility.cache;

import java.util.List;

public abstract class AbstractCollection<T> implements Cacheable<T> {

    private List<?> data;

    public AbstractCollection(List<?> data) 
    {
        super();
        this.data = data;
    }

    public List<?> getList()
    {
        return this.data;
    }
    
}
