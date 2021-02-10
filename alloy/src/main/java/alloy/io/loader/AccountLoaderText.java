package alloy.io.loader;

import alloy.gameobjects.player.Account;
import alloy.utility.settings.AccountSettings;
import io.DataLoader;
import io.FileReader;

/**
 * loader for loading accounts
 */
public class AccountLoaderText extends DataLoader<Account, String> {

    @Override
    public Account load(String file) 
    {
        String[] args = FileReader.read(file);
        String[][] accountArray = configureAccountArray(args);

        String balString = loadSetting(Account.BAL, accountArray);

        int bal = Integer.parseInt(balString);

        AccountSettings settings = new AccountSettings();
        settings.setBal(bal)
                .setPath(file);

        Account a = new Account( settings );

        return a;
    }

    /**
     * 
     * @param args the array of newlines from the file
     * @return a {@link String}[][] that is properly split up   
     */
    private String[][] configureAccountArray(String[] args) 
    {
        String[][] sArgs = new String[args.length][];

        int i = 0;
        for (String s : args)
            sArgs[i] = s.split(">");
        
        return sArgs;
    }

    /**
     * 
     * @param s settings that is being searched for
     * @param accountArray array of things that is being searched in
     * @return the corresponding value for the string passed
     */
    private static String loadSetting(String s, String[][] accountArray) 
    {
        for (String[] strings : accountArray) 
        {
            if (strings[0].equalsIgnoreCase(s))
                return strings[1];
        }
        return null;
    }
    
}
