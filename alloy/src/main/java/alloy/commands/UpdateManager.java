package alloy.commands;

import java.util.ArrayList;
import java.util.List;

import alloy.builder.loaders.ServerLoaderText;
import alloy.gameobjects.Server;
import alloy.gameobjects.player.Account;
import alloy.utility.discord.AlloyCommandListener;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.event.ServerJoinListener;
import io.Saver;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class UpdateManager extends AlloyCommandListener {

    private List<ServerJoinListener> listeners;

    public UpdateManager() 
    {
        super(UpdateManager.class.getName());
        this.listeners = new ArrayList<ServerJoinListener>();
    }

    @Override
    public void onGuildJoin(GuildJoinEvent e) 
    {
        this.checkFileSystem(e);

        for (ServerJoinListener l : listeners)
            l.onServerJoin();

        String code = "CANNOT CREATE INVITE";
        try {
            code = e.getGuild().getDefaultChannel().createInvite().complete().getCode();
        } catch (Exception ex) { }

        this.logger.info("I have joined the server " + e.getGuild().getName() + "\nto join this guild, go to thje link discord.gg/" + code);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) 
    {
        super.onGuildMessageReceived(e);
        if (!validSender(e))
            return;
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent e) 
    {
        this.deleteFileSystem(e);

        for (ServerJoinListener l : listeners)
            l.onServerLeave();
        
        this.logger.info("I have left the server " + e.getGuild().getName());
    }

    private void deleteFileSystem(GuildLeaveEvent e) 
    {
        Saver.deleteFiles(AlloyUtil.ALLOY_PATH + "res\\servers\\" + e.getGuild().getId());
    }

    private void checkFileSystem(GuildJoinEvent e) 
    {
        AlloyUtil.checkFileSystem(e.getGuild().getIdLong(), e.getGuild().getDefaultChannel());
    }

    public void setListener(ServerJoinListener l) 
    {
        this.listeners.add(l);
    }
    
    public boolean removeListener(ServerJoinListener l)
    {
        return this.listeners.remove(l);
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) 
    {
        String path = getGuildPath(event.getGuild());
        path += "\\users\\" + event.getUser().getIdLong();
        Saver.deleteFiles(path);    
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) 
    {
        if (e.getUser().isBot())
            return;

        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        long id = 0l;

        try 
        {
            id = e.getUser().getIdLong();    
        } catch (Exception ex) 
        {
            ex.printStackTrace();
        }

        checkUserFile(id, s);
    }

        /**
     * 
     * @param id id of the user in question
     * @param s server in which said user is a member of
     * @return true if a new file is created
     */
    protected boolean checkUserFile(Long id , Server s) 
    {
        String path = s.getPath() + "\\users\\" + id;
        if (Saver.newFolder(path))
        {
            createUserFile(path , s);
            return true;
        }
        return false;
    }

    private void createUserFile(String path , Server s) 
    {
        String accountPath = path + "\\account.txt";
        String rankPath = path + "\\rank.txt";
        String buildingPath = path + "\\buildings.txt";
        String warningPath = path + "\\warnings.txt";
            
        String[] acc = {
        Account.BAL + ">" + s.getStartingBalance()
        };

        String[] rank = {
            "" + 0
        };

        Saver.saveNewFile(accountPath, acc);
        Saver.saveNewFile(rankPath, rank);
        Saver.saveNewFile(buildingPath);
        Saver.saveNewFile(warningPath);
    }

}
