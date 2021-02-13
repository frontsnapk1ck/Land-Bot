package utility.cache;

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

    @Override
    public long getKeepTime() 
    {
        return DEFAULT_TIME;
    }
    
}
