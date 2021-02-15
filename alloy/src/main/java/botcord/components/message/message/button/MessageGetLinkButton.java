package botcord.components.message.message.button;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Set;

import alloy.utility.discord.DisUtil;
import botcord.BotCord;
import botcord.components.message.message.button.util.AbstractMessageButtons;
import botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.Message;

@SuppressWarnings("serial")
public class MessageGetLinkButton extends AbstractMessageButtons {

    public MessageGetLinkButton(Message message) 
    {
        super(message);
        init();
        config();
	}

	@Override
    public void init() 
    {
    }
    
    @Override
    public void config() 
    {
        setImage();
    }

    @Override
    public void update() 
    {
        updateImage();
    }

    @Override
    protected void onRightClick(Set<MouseModifiers> modifiers) 
    {
        String link = DisUtil.getLink(this.getMessage());
        if (modifiers.contains(MouseModifiers.SHIFT))
            BotCord.LOGGER.info("MessageGetLinkButton", "the link you requested is\n" + link);
        
        StringSelection contents = new StringSelection(link);
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        c.setContents(contents, contents);
    }

    @Override
    protected String getImageLink() 
    {
        return BCUtil.LINK_ICON;
    }
    
}
