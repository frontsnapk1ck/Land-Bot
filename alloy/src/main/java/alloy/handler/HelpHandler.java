package alloy.handler;

import java.util.ArrayList;
import java.util.List;

import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import io.FileReader;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class HelpHandler {

    public static List<String> loadHelpFiles() 
    {
        List<String> out = new ArrayList<String>();
        String[] rawFiles = FileReader.readFolderContents(AlloyUtil.HELP_FOLDER_PATH);
        for (String string : rawFiles) 
            out.add(AlloyUtil.HELP_FOLDER_PATH + AlloyUtil.SUB + string);
        return out;
	}

    public static MessageEmbed loadEmbed(String path) 
    {
        String out = "";
        String[] message = FileReader.read(path);
        for (String string : message)
            out += string + "\n";
        
        Template t = Templates.help(out);
        return t.getEmbed();
	}
    
}
