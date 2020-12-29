package alloy.templates;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class Template {

    private String message;
    private String title;

    private String footerName;
    private String footerURL;
    private String titleURL;
    private List<Field> fields;


    public Template(String title, String s) 
    {
        this.title = title;
        this.titleURL = "";
        this.message = s;
        this.fields = new ArrayList<Field>();
    }

    public MessageEmbed getEmbed()
    {
        EmbedBuilder eb = new EmbedBuilder();
        if (titleURL == "")
            eb.setTitle(title);
        else
            eb.setTitle(title, titleURL);
        eb.setDescription(message);
        eb.setFooter(footerName, footerURL);

        for (Field field : fields)
            eb.addField(field);
        
        return eb.build();
    }

    public String getText()
    {
        String out = title;
        out += "\n";
        out += message;
        return out;
    }

    public String getFooterName() {
        return footerName;
    }

    public String getFooterURL() {
        return footerURL;
    }

    public void setFooterName(String footerName) {
        this.footerName = footerName;
    }

    public void setFooterURL(String footerURL) {
        this.footerURL = footerURL;
    }

    public void addFeild(String name, String value, boolean inline) 
    {
        Field f = new Field(name, value, inline);
        this.fields.add(f);
	}

    public void setTitle(String string, String rickroll) 
    {

	}
    
}
