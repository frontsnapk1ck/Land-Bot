package frontsnapk1ck.alloy.input.device;

import frontsnapk1ck.alloy.utility.error.CommandTypeParseException;
import frontsnapk1ck.input.device.InputType;

public class AlloyInputType extends InputType {

    protected AlloyInputType(String id)
    {
        super(id);
    }

    public static final AlloyInputType CONSOLE = new AlloyInputType("console");
    public static final AlloyInputType DISCORD = new AlloyInputType("discord");
    public static final AlloyInputType BOTH    = new AlloyInputType("both");

    public static InputType parse(String type) throws CommandTypeParseException 
    {
        if (type.equalsIgnoreCase("DISCORD"))
            return DISCORD;
        
        if (type.equalsIgnoreCase("CONSOLE"))
            return CONSOLE;
        
        if (type.equalsIgnoreCase("BOTH"))
            return BOTH;
        
        throw new CommandTypeParseException( type );
    }

    
}
