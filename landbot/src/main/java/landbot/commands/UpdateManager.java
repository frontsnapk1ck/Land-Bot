package landbot.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import landbot.io.FileReader;
import landbot.io.Saver;
import landbot.utility.ServerJoinListener;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UpdateManager extends ListenerAdapter {

    private List<ServerJoinListener> listeners;

    public UpdateManager() 
    {
        this.listeners = new ArrayList<ServerJoinListener>();
	}

	@Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) 
    {
        this.checkFileSystem(e);
    }

    private void checkFileSystem(GuildMessageReceivedEvent e) 
    {
        this.checkFileSystem(e.getGuild().getName() , e.getGuild().getDefaultChannel());        
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

    private void checkFileSystem(String nameN, TextChannel defaultChannel) 
    {
        File dir = new File("landbot\\res\\servers\\" + nameN);
        if (!dir.exists()) 
        {
            dir.mkdir();
            File u = new File( dir.getPath() + "\\users");
            File s = new File( dir.getPath() + "\\settings");
            File buS = new File( s.getPath() + "\\buildings.txt");
            File boS = new File( s.getPath() + "\\bot.settings");
            File woO = new File( s.getPath() + "\\work.options");

            u.mkdir();
            s.mkdir();
            try 
            {
                buS.createNewFile();
                boS.createNewFile();
                woO.createNewFile();

                String defaultChannelS = defaultChannel.getId();
                String[] boSS = { "prefix:!", "starting balance:1000", "cooldown:10", "role assign on buy:false",
                        "spam channel:" + defaultChannelS };

                Saver.saveOverwite(boS.getPath(), boSS);

                String[] buSS = { "shack:2500:15", "apartment:7500:50", "townhome:14500:110", "bungalow:18000:175",
                        "ranch style house:23500:215", "cottage:25750:250", "cabin:29500:300", "chalet:35000:375",
                        "carriage house:42000:500", "craftsman home:57500:750", "mansion:69000:1000",
                        "contemporary mansion:100000:1500", "castle:150000:2000" };

                Saver.saveOverwite(buS.getPath(), buSS);

                String[] woOS = { "you work at mcdonalds", "you work at burger king", "you work at walmart" };

                Saver.saveOverwite(woO.getPath(), woOS);

            }

            catch (IOException ex) {
                ex.printStackTrace();
            }
            
            for (ServerJoinListener l : listeners) 
                l.onServerJoin();
        }
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
