package landbot.utility;

import java.io.File;
import java.io.IOException;

import landbot.gameobjects.Server;
import landbot.io.Saver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AlloyCommandListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) 
    {
        String name = e.getGuild().getName();
        TextChannel channel = e.getGuild().getDefaultChannel();
        checkFileSystem( name , channel );
    }
 
    public void checkFileSystem( String guildname , TextChannel defaultChannel )
    {
        File dir = new File("landbot\\res\\servers\\" + guildname);
        if (!dir.exists()) 
        {
            dir.mkdir();
            File u = new File( dir.getPath() + "\\users");
            File s = new File( dir.getPath() + "\\settings");
            File buS = new File( s.getPath() + "\\buildings.txt");
            File boS = new File( s.getPath() + "\\bot.settings");
            File woO = new File( s.getPath() + "\\work.options");
            File luA = new File( s.getPath() + "\\rank.ups");

            u.mkdir();
            s.mkdir();
            try 
            {
                buS.createNewFile();
                boS.createNewFile();
                woO.createNewFile();
                luA.createNewFile();

                String defaultChannelS = defaultChannel.getId();
                String[] boSS = {   Server.PREFIX + ":!" , 
                                    Server.STARTING_BALANCE + ":1000" ,
                                    Server.COOLDOWN + ":10" ,
                                    Server.ROLE_ASSIGN_ON_BUY + ":false",
                                    Server.SPAM_CHANNEL + ":" + defaultChannelS ,
                                    Server.BALCKLISTED_CHANNENLS + ":" , 
                                    Server.XP_COOLDOWN + ":4"
                                };

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
            
        }
    }

    protected String getGuildPath(Guild guild)
    {
        return "landbot\\res\\servers\\" + guild.getName();
    }

}
