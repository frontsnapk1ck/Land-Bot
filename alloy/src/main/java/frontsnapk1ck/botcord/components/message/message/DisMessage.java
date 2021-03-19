package frontsnapk1ck.botcord.components.message.message;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import org.kefirsf.bb.BBProcessorFactory;
import org.kefirsf.bb.ConfigurationFactory;
import org.kefirsf.bb.TextProcessor;

import frontsnapk1ck.botcord.components.gui.BCPanel;
import frontsnapk1ck.botcord.components.gui.base.BaseBCLabel;
import frontsnapk1ck.botcord.components.message.message.attachment.AbstractMessageAttachment;
import frontsnapk1ck.botcord.components.message.message.attachment.DisMessageAttachment;
import frontsnapk1ck.botcord.components.message.message.button.UserButton;
import frontsnapk1ck.botcord.components.message.message.button.util.DisMessageButtons;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;

@SuppressWarnings("serial")
public class DisMessage extends BCPanel {

    private Message message;
    private MessageTopRow topRow;

    private UserButton userButton;
    private DisMessageButtons messageButtons;
    private BaseBCLabel text;
    private List<AbstractMessageAttachment> attachments;

    public DisMessage ( Message m ) 
    {
        super();
        this.message = m;
        init();
        config();
    }

    @Override
    public void init() 
    {
        this.attachments = new ArrayList<AbstractMessageAttachment>();
        this.userButton = getUserButton();
        this.messageButtons = new DisMessageButtons(this.message);
        this.text = initText();
        this.setBackground(BCUtil.BACKGROUND);
    }

    private BaseBCLabel initText() 
    {
        BaseBCLabel label = new BaseBCLabel();
        if (this.message.getContentRaw().equals(""))
            return label;
        TextProcessor processor = BBProcessorFactory.getInstance().createFromResource(ConfigurationFactory.MARKDOWN_CONFIGURATION_FILE);
        label.setText("<html>" + processor.process(this.message.getContentRaw()) + "</html>");
        label.setForeground(BCUtil.TEXT);
        return label;
    }

    private UserButton getUserButton() 
    {
        if (this.message.isFromGuild())
            return new UserButton(this.message.getMember());
        else 
            return new UserButton(this.message.getAuthor());
    }

    @Override
    public void config() 
    {
        makeAtt();
        addCompsSelf();
        updateLayout();
        this.setToolTipText("Message");
    }

    private void updateLayout()
    {
        int comps = this.getNumComps();
        this.setLayout(new GridLayout(comps,1,10,10));
    }

    private int getNumComps()
    {
        return this.attachments.size() + 2;
    }

    private void addCompsSelf()
    {
        this.topRow = new MessageTopRow(this.messageButtons, this.userButton);
        this.add(this.topRow);
        this.add(this.text);
        for (AbstractMessageAttachment abstractMessageAttachment : attachments) 
            this.add(abstractMessageAttachment);
    }

    private void makeAtt() 
    {
        if ( this.message.getAttachments().size() == 0 )
            return;
        List<Attachment> atts = this.message.getAttachments();
        for (Attachment attachment : atts) 
            this.attachments.add(DisMessageAttachment.load(attachment.getUrl()));
    }

    @Override
    public void update() 
    {
        updateLayout();
    }

	public void setListeners(List<BCListener> listeners) 
    {
        this.messageButtons.setListeners(listeners);
        this.userButton.setListeners(listeners);

	}
    
}
