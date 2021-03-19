package frontsnapk1ck.alloy.utility.settings;

public abstract class AbstractSettings {

    private String path;

    public abstract AbstractSettings copy();

    public String getPath() 
    {
        return path;
    }

    public AbstractSettings setPath(String path) 
    {
        this.path = path;
        return this;
    }

}
