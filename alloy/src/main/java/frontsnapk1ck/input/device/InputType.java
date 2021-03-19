package frontsnapk1ck.input.device;

public abstract class InputType {

    private String id;

    protected InputType(String id)
    {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InputType)
        {
            InputType t = (InputType) obj;
            return t.id.equalsIgnoreCase(this.id);
        }
        return true;
    }    
    
}
