package alloy.utility.discord;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class AlloyCommandListener extends ListenerAdapter {

    protected Logger logger;

    public AlloyCommandListener(String name) 
    {
        this.logger = LoggerFactory.getLogger(name);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) 
    {
        long id = e.getGuild().getIdLong();
        TextChannel channel = e.getGuild().getDefaultChannel();
        AlloyUtil.checkFileSystem( id , channel );
    }

    protected static String getGuildPath(Guild guild)
    {
        return AlloyUtil.ALLOY_PATH + "res\\servers\\" + guild.getId();
    }

    public boolean hasPerm(GuildMessageReceivedEvent e, DisPerm p, boolean b) 
    {
        boolean valid = DisPermUtil.checkPermission(e.getMember(), p);
        if (!valid && b) 
            sendInvalidPerms( e , p );
        return valid;
    }

    private void sendInvalidPerms(GuildMessageReceivedEvent e, DisPerm p) 
    {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Insufficent Permisions");
        String message = "you can only use this command if you have the `" + p.getName() + "` permission";
        eb.addField(new Field( message , "",true));
        e.getChannel().sendMessage(eb.build()).queue();
    }

    protected boolean validSender(GuildMessageReceivedEvent e) 
    {
        List<Long> blacklisted = AlloyUtil.getBlacklisted();
        boolean isBlack = blacklisted.contains(e.getAuthor().getIdLong());
        boolean isBot = e.getAuthor().isBot();

        return !isBlack && !isBot;
    }

}
