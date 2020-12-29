package alloy.builder.loaders;

import alloy.builder.DataLoader;
import alloy.gameobjects.player.Account;
import alloy.utility.settings.AccountSettings;
import io.FileReader;

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

    private String[][] configureAccountArray(String[] args) 
    {
        if (args == null)
            System.out.println("AccountLoaderText.configureAccountArray()");

        String[][] sArgs = new String[args.length][];

        int i = 0;
        for (String s : args)
            sArgs[i] = s.split(">");
        
        return sArgs;
    }

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
