package alloy.utility.settings;

public class AccountSettings extends AbstractSettings {

    private int bal;

    public AccountSettings() 
    {
        this.bal = 0;
        setPath("");
    }

    public int getBal() {
        return bal;
    }

    public AccountSettings setBal(int bal) 
    {
        this.bal = bal;
        return this;
    }

    @Override
    public AccountSettings setPath(String path) 
    {
        super.setPath(path);
        return this;
    }

    public AccountSettings copy() 
    {
        AccountSettings settings = new AccountSettings();
        settings.setBal(bal)
                .setPath(getPath());
        
        return settings;
	}
    
}
