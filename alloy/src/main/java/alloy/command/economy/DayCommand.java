package alloy.command.economy;

import java.util.List;

import alloy.command.util.AbstractCommand;
import alloy.gameobjects.player.Player;
import alloy.input.discord.AlloyInputData;
import alloy.io.loader.PlayerLoaderText;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;


public class DayCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() 
    {
        return DisPerm.ADMINISTRATOR;
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        User author = data.getUser();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);
        
        if (!DisPermUtil.checkPermission(m, getPermission()))
        {
            Template t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("DayCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);   
            return;
        }

        String path = AlloyUtil.getGuildPath(g) + AlloyUtil.USER_FOLDER;
        PlayerLoaderText plt = new PlayerLoaderText();
        List<Player> players = plt.loadALl(path);
        for (Player p : players)
            p.day();

        Template t = Templates.daySuccess(g);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("DayCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);  
    }
    
}
