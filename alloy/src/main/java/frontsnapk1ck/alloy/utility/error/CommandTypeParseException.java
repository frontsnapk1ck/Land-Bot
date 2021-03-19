package frontsnapk1ck.alloy.utility.error;

public class CommandTypeParseException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 12443534654432323L;
    
    private String reason;
    
    public CommandTypeParseException() 
    {
        this.reason = "Invalid String provided, no CommandType Found";
    }

    public CommandTypeParseException(String type) 
    {
        this.reason = "Invalid String provided, no CommandType Found - " + type;
	}

	public String getReason() {
        return reason;
    }

}
