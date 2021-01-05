package alloy.builder.loaders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import alloy.builder.DataLoader;
import alloy.command.util.PunishType;
import alloy.gameobjects.Case;
import alloy.utility.settings.CaseSettings;
import io.FileReader;

/**
 * loader for loading cases
 */
public class CaseLoaderText extends DataLoader<Case, String> {

    @Override
    public Case load(String file) 
    {
        String[] arr = FileReader.read(file);
        String[][] caseArray = configureCaseArray(arr);

        String numString        = loadSetting(Case.NUM, caseArray);
        String issuerString     = loadSetting(Case.ISSUER, caseArray);
        String punishString     = loadSetting(Case.PUNISH_TYPE, caseArray);
        String reasonString     = loadSetting(Case.REASON, caseArray);
        String messageIDString  = loadSetting(Case.MESSAGE_ID, caseArray);

        int num = Integer.parseInt(numString);
        Long issuer = Long.parseLong(issuerString);
        PunishType punishType = PunishType.parseType(punishString);
        String reason = String.valueOf(reasonString);
        Long messageID = Long.parseLong(messageIDString);

        CaseSettings settings = new CaseSettings();
        settings.setIssuer(issuer)
                .setMessageId(messageID)
                .setNum(num)
                .setPunishType(punishType)
                .setReason(reason)
                .setPath(file);

        Case c = new Case(settings);

        return c;

        
    }

    /**
     * 
     * @param args the array of newlines from the file
     * @return a {@link String}[][] that is properly split up   
     */
    private String[][] configureCaseArray(String[] args) 
    {
        String[][] sArgs = new String[args.length][];
        for (int i = 0; i < sArgs.length; i++) 
        {
            String s = args[i];
            String[] split = s.split(":");
            sArgs[i]= new String[split.length];
            for (int j = 0; j < split.length; j++) 
                sArgs[i][j] = split[j];
        }
        return sArgs;
    }

    /**
     * 
     * @param s settings that is being searched for
     * @param caseArray array of things that is being searched in
     * @return the corrisponding value for the string passed
     */
    private static String loadSetting(String s, String[][] caseArray) 
    {
        for (String[] strings : caseArray) 
        {
            if (strings[0].equalsIgnoreCase(s) && strings.length > 1)
                return strings[1];
        }
        return null;
    }

    @Override
    public List<Case> loadALl(String file) 
    {
        List<Case> cases = new ArrayList<Case>();
        File f = new File(file);
        
        for (File sub : f.listFiles())
        {
            String path = sub.getPath();
            Case p = this.load(path);
            cases.add(p);
        }
        return cases;
    }
}
