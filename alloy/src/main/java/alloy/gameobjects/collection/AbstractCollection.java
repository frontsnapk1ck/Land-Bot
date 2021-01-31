package alloy.gameobjects.collection;

import java.util.List;

import alloy.gameobjects.cache.Cacheable;

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
