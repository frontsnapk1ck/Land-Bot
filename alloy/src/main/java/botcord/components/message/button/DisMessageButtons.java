package botcord.components.message.button;

import java.awt.GridLayout;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import botcord.components.gui.BCPanel;
import botcord.components.message.button.util.AbstractMessageButtons;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

@SuppressWarnings("serial")
public class DisMessageButtons extends BCPanel {

    public static final int COMP_SIZE = 30;

    private Message message;
    private Set<ButtonType> perms;
    private List<AbstractMessageButtons> buttons;

    public DisMessageButtons(Message m) 
    {
        this.message = m;
        init();
        config();
    }

    @Override
    public void init() 
    {
        perms = getPerms();
    }

    private Set<ButtonType> getPerms() 
    {
        if (!message.isFromGuild())
            return getPermsPC();
        else 
            return getPermsGuild();
    }

    private Set<ButtonType> getPermsGuild() 
    {
        Guild g = this.message.getGuild();
        Member m = g.getSelfMember();
        if (DisPermUtil.checkPermission(m, DisPerm.MESSAGE_MANAGE))
            return ButtonType.manage();
        if (message.getAuthor().equals(m.getUser()))
            return ButtonType.all();
        return ButtonType.normal();
    }

    private Set<ButtonType> getPermsPC() 
    {
        if ( message.getAuthor().equals(message.getJDA().getSelfUser()))
            return ButtonType.all();
        else 
            return ButtonType.dm();
    }

    @Override
    public void config() 
    {
        this.updateToolTip();
        this.makeButtons();
        this.configButtons();
        this.updateBounds();
        this.updateLayout();
    }

    private void updateLayout() 
    {
        this.setLayout(new GridLayout(1 , this.buttons.size() , 10 , 10));
    }

    private void updateBounds() 
    {
        final int w = COMP_SIZE;
        final int h = COMP_SIZE;
        for (AbstractMessageButtons abstractMessageButtons : buttons) 
            abstractMessageButtons.setSize(w, h);
    }

    private void updateToolTip() 
    {
        this.setToolTipText("Message Buttons");
    }

    private void configButtons() 
    {
        for (AbstractMessageButtons b : buttons) 
            this.add(b);
    }

    private void makeButtons() 
    {
        for (ButtonType bt : perms) 
            this.buttons.add(makeButton(bt));
    }

    private AbstractMessageButtons makeButton(ButtonType bt) 
    {
        if (bt == ButtonType.REPLY)
            return new MessageReplyButton(message);
        if (bt == ButtonType.EDIT)
            return new MessageEditButton(message);
        if (bt == ButtonType.PIN)
            return new MessagePinButton(message);
        if (bt == ButtonType.LINK)
            return new MessageGetLinkButton(message);
        if (bt == ButtonType.DELETE)
            return new MessageDeleteButton(message);
        else return null;
    }

    @Override
    public void update() 
    {
        // TODO Auto-generated method stub
    }

    private enum ButtonType 
    {
        REPLY,
        EDIT,
        PIN,
        LINK,
        DELETE,;

        public static Set<ButtonType> all()
        {
            return Collections.synchronizedSet(EnumSet.allOf(ButtonType.class));
        }

        public static Set<ButtonType> manage()
        {
            Set<ButtonType> set = Collections.synchronizedSet(EnumSet.allOf(ButtonType.class));
            set.remove(EDIT);

            return set;
        }

        public static Set<ButtonType> normal()
        {
            Set<ButtonType> set = Collections.synchronizedSet(EnumSet.allOf(ButtonType.class));
            set.remove(EDIT);
            set.remove(PIN);
            set.remove(DELETE);

            return set;
        }

        public static Set<ButtonType> dm()
        {
            Set<ButtonType> set = Collections.synchronizedSet(EnumSet.allOf(ButtonType.class));
            set.remove(EDIT);
            set.remove(DELETE);

            return set;

        }
    }
    
}
