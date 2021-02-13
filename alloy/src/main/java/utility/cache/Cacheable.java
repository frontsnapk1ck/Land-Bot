package utility.cache;

public interface Cacheable<T> {

    public static final long DEFAULT_TIME = 5000L;
    public static final long FOREVER = -1L;

    public T getData();

    /**
     * 
     * @return the time in milliseconds that an object should be kept in the cache. a time of less than 0 will keep it in forever.
     */
    public long getKeepTime();
    
}
