package landbot.utility;

import landbot.Server;
import landbot.io.Saver;
import landbot.player.Account;
import landbot.player.Rank;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class PlayerCommand extends ListenerAdapter {


    protected abstract void help(GuildMessageReceivedEvent e);


    /**
     * 
     * @param id id of the user in question
     * @param s server in which said user is a member of
     * @return true if a new file is created
     */
    public boolean checkUserFile(Long id , Server s) 
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
            Rank.XP + ">" + 0
        };

        Saver.saveNewFile(accountPath, acc);
        Saver.saveNewFile(rankPath, rank);
        Saver.saveNewFile(buildingPath);
    }
    
}
