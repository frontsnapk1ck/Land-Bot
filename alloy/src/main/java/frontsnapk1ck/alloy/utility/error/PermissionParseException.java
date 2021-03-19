package frontsnapk1ck.alloy.utility.error;

import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import net.dv8tion.jda.api.Permission;


public class PermissionParseException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 235547567544543L;
    private String reason;
    
    public PermissionParseException() 
    {
        this.reason = "Invalid String provided, no Permission Found";
    }

    public PermissionParseException(String string) 
    {
        this.reason = "Invalid String provided, no Permission Found: " + string;
    }

    public PermissionParseException(DisPerm perm) 
    {
        this.reason = "DisPerm Not Recongnized as a Stock Perm - " + perm.getName();
	}

    public PermissionParseException(Permission perm) 
    {
        this.reason = "permmision could not be parsed (how strange) - " + perm.getName();
	}

	public String getReason() {
        return reason;
    }
    
}

