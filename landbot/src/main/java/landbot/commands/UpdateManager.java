package landbot.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import landbot.io.FileReader;
import landbot.io.Saver;
import landbot.utility.AlloyCommandListener;
import landbot.utility.event.ServerJoinListener;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class UpdateManager extends AlloyCommandListener {

    private List<ServerJoinListener> listeners;

    public UpdateManager() 
    {
        this.listeners = new ArrayList<ServerJoinListener>();
	}

    @Override
    public void onGuildJoin(GuildJoinEvent e) 
    {
        this.checkFileSystem(e);

        for (ServerJoinListener l : listeners) 
            l.onServerJoin();
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
    public void onGuildUpdateName(GuildUpdateNameEvent e) {
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

}
