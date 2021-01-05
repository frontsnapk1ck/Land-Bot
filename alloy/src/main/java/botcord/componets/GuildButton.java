package botcord.componets;

import java.awt.Event;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import alloy.main.Alloy;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import botcord.BotCord;
import botcord.event.PressListener;
import botcord.event.ScreenSwitchEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

@SuppressWarnings("serial")
public class GuildButton extends JButton {

    private List<PressListener> listeners;
    private Guild guild;

    public GuildButton(Guild g) 
    {
        super();
        init(g);
        config(g);
    }

    private void init(Guild g) 
    {
        this.listeners = new ArrayList<PressListener>();
        this.guild = g;
        this.setBackground(BotCord.BACKGROUND);
        this.setBorder(null);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) 
    {
        super.processMouseEvent(e);
        System.err.println(this.getBounds());
        this.repaint();
    }

    private void config(Guild g) 
    {
        try 
        {
            Image img = getImage(g.getIconUrl());
            this.setIcon(new ImageIcon(img));
            this.congifgToolTip(g);
            this.configAction(g);
        }
        catch (IOException e) 
        {
            Alloy.LOGGER.error("GuildButton", e);
            e.printStackTrace();
        }

    }

    private void configAction(Guild g) 
    {
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) 
            {
                ScreenSwitchEvent e = new ScreenSwitchEvent(ev).setGuild(g);
                for (PressListener l : listeners)
                    l.onSwitchScreenPress(e);
            }
        });
    }

    private void congifgToolTip(Guild g) 
    {
        String code = "<html><body><h1> " + g.getName() + "</h1>" + 
                        "<p>Owned by: "   + g.getOwner().getUser().getAsTag()     + "</p>" +
                        "<p>Is Admin: "         + isAdmin(g)            + "</p>"                                    +
                        "<p>Members: "          + getMembers(g)         + " | Bots: "   + getBots(g) + "</p>" + 
                        "<p>Boost: "            + g.getBoostTier()      + " | Boosts: " + g.getBoostCount() + "</p>" +
                        "</body></html>";
        this.setToolTipText(code);
    }

    private int getBots(Guild g) 
    {
        int bot = 0;
        List<Member> members = g.getMembers();
        for (Member m : members) 
        {
            if (m.getUser().isBot())
                bot++;
        }
        return bot;
    }

    private int getMembers(Guild g) 
    {
        int mem = 0;
        List<Member> members = g.getMembers();
        for (Member m : members) 
        {
            if (!m.getUser().isBot())
                mem++;
        }
        return mem;
    }

    private boolean isAdmin(Guild g) 
    {
        List<DisPerm> perms = DisPermUtil.parsePerms(g.getSelfMember().getPermissions());
        for (DisPerm p : perms) 
        {
            if (p.equals(DisPerm.ADMINISTRATOR))
                return true;
        }
        return false;
    }

    private Image getImage(String urlS) throws IOException 
    {
        if (urlS == null)
            urlS = BotCord.DEFUALT_DISCORD_PHOTO;
        URL url = new URL(urlS);
        Image img = ImageIO.read(url);
        return img;
    }

    public void updateImage(int w, int h) 
    {
        if ( w == 0 || h == 0)
            return;
        Icon ic = this.getIcon();
        if (ic instanceof ImageIcon)
        {
            Image img = ((ImageIcon) ic).getImage();
            img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            ImageIcon newIc = new ImageIcon(img);
            this.setIcon(newIc);
        }
    }

    public void add(PressListener l)
    {
        this.listeners.add(l);
    }

    public void update(int w, int h) 
    {
        this.congifgToolTip(guild);
        this.updateImage(w, h);
	}

}
