package botcord.components.button;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import alloy.main.Alloy;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import botcord.components.util.BotCordButton;
import botcord.event.BotCordListener;
import botcord.event.PressEvent;
import botcord.event.PressTarget;
import botcord.util.BotCordColors;
import botcord.util.BotCordLinks;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

@SuppressWarnings("serial")
public class GuildButton extends BotCordButton {

    private Guild guild;

    public GuildButton(Guild guild) {
        this.guild = guild;
        init();
        config();
    }

    @Override
    public void init() {
        this.setBackground(BotCordColors.BACKGROUND);
        this.setBackground(null);
    }

    @Override
    public void config() {
        setImage();
    }

    private void setImage() {
        try {
            Image img = getImage(this.guild.getIconUrl());
            this.setIcon(new ImageIcon(img));
            this.configToolTip();
            this.configAction();
        } catch (IOException e) {
            Alloy.LOGGER.error("GuildButton", e);
            e.printStackTrace();
        }
    }

    public void updateImage() {
        int w, h;
        w = this.getWidth();
        h = this.getHeight();

        if (w == 0 || h == 0)
            return;
        Icon ic = this.getIcon();
        if (ic instanceof ImageIcon) {
            Image img = ((ImageIcon) ic).getImage();
            img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            ImageIcon newIc = new ImageIcon(img);
            this.setIcon(newIc);
        }
    }

    private void configAction() {
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                PressEvent e = new PressEvent(PressTarget.GUILD);
                e.setData(getGuild());
                for (BotCordListener l : getListeners())
                    l.onPress(e);
            }
        });
    }

    private void configToolTip() {
        Guild g = this.guild;
        String code = "<html><body><h1> " + g.getName() + "</h1>" + "<p>Owned by: " + g.getOwner().getUser().getAsTag()
                + "</p>" + "<p>Is Admin: " + isAdmin(g) + "</p>" + "<p>Members: " + getMembers(g) + " | Bots: "
                + getBots(g) + "</p>" + "<p>Boost: " + g.getBoostTier() + " | Boosts: " + g.getBoostCount() + "</p>"
                + "</body></html>";
        this.setToolTipText(code);
    }

    private int getBots(Guild g) {
        int bot = 0;
        List<Member> members = g.getMembers();
        for (Member m : members) {
            if (m.getUser().isBot())
                bot++;
        }
        return bot;
    }

    private int getMembers(Guild g) {
        int mem = 0;
        List<Member> members = g.getMembers();
        for (Member m : members) {
            if (!m.getUser().isBot())
                mem++;
        }
        return mem;
    }

    private boolean isAdmin(Guild g) {
        List<DisPerm> perms = DisPermUtil.parsePerms(g.getSelfMember().getPermissions());
        for (DisPerm p : perms) {
            if (p.equals(DisPerm.ADMINISTRATOR))
                return true;
        }
        return false;
    }

    private Image getImage(String urlS) throws IOException {
        if (urlS == null)
            urlS = BotCordLinks.DEFAULT_DISCORD_PHOTO;
        URL url = new URL(urlS);
        Image img = ImageIO.read(url);
        return img;
    }

    public Guild getGuild() {
        return guild;
    }

    @Override
    public void update() {
        this.updateImage();
        this.configToolTip();
    }

}
