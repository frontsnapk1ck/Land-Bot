package alloy.io.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import alloy.gameobjects.Warning;
import alloy.utility.settings.WarningSettings;
import io.DataLoader;
import io.FileReader;

public class WarningLoaderText extends DataLoader<Warning, String> {

    @Override
    public List<Warning> loadALl(String file) 
    {

        List<Warning> warnings = new ArrayList<Warning>();

        File f = new File(file);

        if (!f.exists())
            f.mkdir();
            
        for (File sub : f.listFiles())
        {
            Warning w = load(sub.getAbsolutePath());
            if (w != null)
                warnings.add(w);
        }

        return warnings;
    }

    public Warning load(String path) 
    {
        String[] arr = FileReader.read(path);
        String[][] s = configureWarningArray(arr);

        if (s.length == 0)
            return null;

        String reasonString   = s[0][1];
        String issuerString   = s[1][1];
        String idString       = s[2][1];
        String targetID       = s[3][1];

        String reason = String.valueOf(reasonString);
        Long issuer =    Long.parseLong(issuerString);
        String id =     String.valueOf(idString);
        long target = Long.parseLong(targetID);


        WarningSettings settings = new WarningSettings();
        settings.setId(id)
                .setIssuer(issuer)
                .setReason(reason)
                .setPath(path)
                .setTarget(target);
            
        Warning w = new Warning( settings );

        return w;

    }

    private String[][] configureWarningArray(String[] args) 
    {
        String[][] sArgs = new String[args.length][];

        int i = 0;
        for (String s : args)
        {
            sArgs[i] = s.split(":");
            i++;
        }
        
        return sArgs;
    }
    
}
