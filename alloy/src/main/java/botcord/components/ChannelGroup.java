package botcord.components;

import java.util.List;

import botcord.components.util.BotCordPanel;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.GuildChannel;

@SuppressWarnings("serial")
public class ChannelGroup extends BotCordPanel {

    private Type type;

    public ChannelGroup(Category category) 
    {
        this.type = Type.CATEGORIZED;
        init();
        config();
	}

    public ChannelGroup(List<GuildChannel> missing) 
    {
        this.type = Type.UNCATEGORIZED;
        init();
        config();
	}

    @Override
    public void init() 
    {
        if (this.isCategorized())
            initCategorized();
        else
            initUnCategorized();
    }

    private void initUnCategorized() 
    {

    }

    private void initCategorized() 
    {

    }

    private boolean isCategorized() 
    {
        return this.getType().equals(Type.CATEGORIZED);
    }

    @Override
    public void config() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

    public Type getType() {
        return type;
    }

    public enum Type
    {
        CATEGORIZED,
        UNCATEGORIZED
    }
    
}
