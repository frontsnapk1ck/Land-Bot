package landbot.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import landbot.builder.loaders.ServerLoaderText;
import landbot.gameobjects.Server;
import landbot.gameobjects.player.Account;
import landbot.io.FileReader;
import landbot.io.Saver;
import landbot.utility.AlloyCommandListener;
import landbot.utility.event.ServerJoinListener;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.order.RoleOrderAction;

public class UpdateManager extends AlloyCommandListener {

    private List<ServerJoinListener> listeners;

    public UpdateManager() 
    {
        super(UpdateManager.class.getName());
        this.listeners = new ArrayList<ServerJoinListener>();
    }

    @Override
    public void onGuildJoin(GuildJoinEvent e) {
        this.checkFileSystem(e);

        for (ServerJoinListener l : listeners)
            l.onServerJoin();

        changeRolePos(e);
    }
    
    private void changeRolePos(GuildJoinEvent e)
    {
        List<Role> alloys = e.getGuild().getRolesByName("Alloy", true);
        if (alloys.size() < 1)
            return;
        Role alloy = alloys.get(0);
        RoleOrderAction roa = e.getGuild().modifyRolePositions();
        int length = e.getGuild().getRoles().size();

        roa.selectPosition(alloy);

        boolean valid = false;
        int offset = 0;
        while(!valid)
        {
            try 
            {
                int pos = length - offset;
                roa.moveTo(pos);
                roa.queue();
                valid = true;
            }
            catch (IllegalStateException ise) 
            {
                valid = false;
            }
            catch (IllegalArgumentException iae)
            {
                valid = false;
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
                return;
            }

            offset ++;
        }

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) 
    {
        super.onGuildMessageReceived(e);
        if (e.getAuthor().isBot())
            return;
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent e) 
    {
        this.deleteFileSystem(e);

        for (ServerJoinListener l : listeners)
            l.onServerLeave();
    }

    private void deleteFileSystem(GuildLeaveEvent e) 
    {
        Saver.deleteFiles("landbot\\res\\servers\\" + e.getGuild().getName());
    }

    private void checkFileSystem(GuildJoinEvent e) 
    {
        this.checkFileSystem(e.getGuild().getName(), e.getGuild().getDefaultChannel());
    }

    @Override
    public void onGuildUpdateName(GuildUpdateNameEvent e) 
    {
        this.checkFileSystem(e.getNewName(), e.getGuild().getDefaultChannel());
        File settings = new File("landbot\\res\\servers\\" + e.getOldName() + "\\settings");
        File users = new File("landbot\\res\\servers\\" + e.getOldName() + "\\users");

        String[] files = FileReader.readFolderContents(users);
        for (String name : files) {
            name.replace(users.getPath(), "");
            String pathN = "landbot\\res\\servers\\" + e.getNewName() + "\\users\\" + name;
            String pathO = "landbot\\res\\servers\\" + e.getOldName() + "\\users\\" + name;
            String[] tmp = FileReader.read(pathO);
            Saver.saveNewFile(pathN);
            Saver.saveOverwite(pathN, tmp);
        }

        files = FileReader.readFolderContents(settings);
        for (String name : files) {
            name.toString().replace(users.getPath(), "");
            String pathN = "landbot\\res\\servers\\" + e.getNewName() + "\\settings\\" + name;
            String pathO = "landbot\\res\\servers\\" + e.getOldName() + "\\settings\\" + name;
            String[] tmp = FileReader.read(pathO);
            Saver.saveNewFile(pathN);
            Saver.saveOverwite(pathN, tmp);
        }

        Saver.deleteFiles("landbot\\res\\servers\\" + e.getOldName());
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
            
        String[] acc = {
        Account.BAL + ">" + s.getStartingBalance()
        };

        String[] rank = {
            "" + 0
        };

        Saver.saveNewFile(accountPath, acc);
        Saver.saveNewFile(rankPath, rank);
        Saver.saveNewFile(buildingPath);
    }

}
