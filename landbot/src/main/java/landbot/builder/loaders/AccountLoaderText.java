package landbot.builder.loaders;

import landbot.builder.DataLoader;
import landbot.io.FileReader;
import landbot.player.Account;

public class AccountLoaderText extends DataLoader<Account, String> {

    @Override
    public Account load(String file) 
    {
        String[] args = FileReader.read(file);
        String[][] accountArray = configureAccountArray(args);

        String balString = loadSetting(Account.BAL, accountArray);

        int bal = Integer.parseInt(balString);

        Account a = new Account(bal, file);

        return a;
    }

    private String[][] configureAccountArray(String[] args) 
    {
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
