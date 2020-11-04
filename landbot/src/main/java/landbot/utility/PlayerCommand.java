package landbot.utility;


import landbot.builder.loaders.ServerLoaderText;
import landbot.gameobjects.Server;
import landbot.gameobjects.player.Account;
import landbot.io.Saver;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class PlayerCommand extends AlloyCommandListener {

    public PlayerCommand(String name) 
    {
        super(name);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) 
    {
        super.onGuildMessageReceived(e);

        if (e.getAuthor().isBot())
            return;

        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        long id = 0l;

        try 
        {
            id = e.getAuthor().getIdLong();    
        } catch (Exception ex) 
        {
            ex.printStackTrace();
        }

        checkUserFile(id, s);

    }

    protected abstract void help(GuildMessageReceivedEvent e);


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
    
    protected boolean validUser(String s)
    {
        try 
        {
            s = s.replace("<@!", "");
            s = s.replace("<@", "");
            s = s.replace(">", "");

            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected String getUserPath(GuildMessageReceivedEvent e, String id) 
    {

        id = id.replace("<@!", "");
        id = id.replace("<@", "");
        id = id.replace(">", "");

        String out = getGuildPath(e.getGuild());
        out += "\\users\\";
        out += id;

        return out;
    }
}
