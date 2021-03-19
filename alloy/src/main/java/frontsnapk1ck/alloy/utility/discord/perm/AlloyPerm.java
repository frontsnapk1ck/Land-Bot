package frontsnapk1ck.alloy.utility.discord.perm;

public class AlloyPerm extends DisPerm {

    public static final AlloyPerm CREATOR   = new AlloyPerm(    "CREATOR"   );
    public static final AlloyPerm CONSOLE   = new AlloyPerm(    "CONSOLE"   );
 
    
    private AlloyPerm ( String name )
    {
        super(name);
    }


    public static AlloyPerm get(DisPerm disPerm) 
    {
		return (AlloyPerm) disPerm;
	}

}
