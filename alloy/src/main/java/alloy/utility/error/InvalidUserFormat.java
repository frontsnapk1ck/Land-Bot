package alloy.utility.error;

public class InvalidUserFormat extends RuntimeException {

    private static final long serialVersionUID = 6383247691824229948L;

    private String s;

    public InvalidUserFormat(String string) 
    {
        this.s = string;
	}

    @Override
    public String toString() 
    {
        return s;
    }
}
