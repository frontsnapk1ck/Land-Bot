package landbot.commands;

import java.util.ArrayList;
import java.util.List;

import landbot.Server;
import landbot.builder.PlayerBuilder;
import landbot.builder.ServerBuilder;
import landbot.io.FileReader;
import landbot.io.Saver;
import landbot.player.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {

    String[] workOptions;
    private List<Player> cooldownUsers;

    public Commands() {
        this.cooldownUsers = new ArrayList<Player>();
    }

    private void createWorkOptions(Server s) 
    {
        this.workOptions = FileReader.read(s.getPath() + "\\settings\\work.options");
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) 
    {
        if (e.getAuthor().isBot())
            return;
        
        Server s = ServerBuilder.buildServer(e.getGuild());
        createWorkOptions(s);

        String[] args = e.getMessage().getContentRaw().split(" ");

        if (!args[0].startsWith(s.getPrefix()) || e.getAuthor().isBot())
            return;

        if (args[0].equalsIgnoreCase(s.getPrefix() + "work"))
            work(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "bal"))
            checkBalance(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "help"))
            help(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "rank"))
            rank(e);
        
        else if (args[0].equalsIgnoreCase(s.getPrefix() + "say"))
            say(e);
        }

    private void say(GuildMessageReceivedEvent e) 
    {

        if (e.getAuthor().getIdLong() == 312743142828933130l)
        {
            String out = e.getMessage().getContentRaw().toString();
            out = out.replace("!say ", "");
            e.getMessage().delete().queue();
            e.getChannel().sendMessage(out).queue();
        }
        
        else if (e.getMember().getPermissions().contains(Permission.ADMINISTRATOR))
        {
            EmbedBuilder em = new EmbedBuilder();
            em.setFooter(e.getAuthor().getName(), e.getAuthor().getAvatarUrl());
            em.setTitle("Announcement");
            String out = e.getMessage().getContentRaw().toString();
            out = out.replace("!say ", "");
            em.setDescription(out);
            e.getChannel().sendMessage(em.build()).queue();
        }
    }

    private void rank(GuildMessageReceivedEvent e) 
    {
        Server s = ServerBuilder.buildServer(e.getGuild());
        long id = e.getAuthor().getIdLong();

        checkUserFile( id , s );

        Player p = PlayerBuilder.load(e.getAuthor().getIdLong() , e.getGuild().getName());

        for (Player player : cooldownUsers) 
        {
            if (player.equals(p))
            {
                sendTooFast(e);
                return;
            }    
        }

        e.getChannel().sendMessage("https://tenor.com/view/rank-talk-selfie-man-eyeglasses-gif-17817029").queue();
        cooldown( e, s.getCooldown() );
    }

    private void checkBalance(GuildMessageReceivedEvent e, String[] args) 
    {
        Server s = ServerBuilder.buildServer(e.getGuild());
        long id = e.getMember().getIdLong();

        if (args.length != 1 )
        {
            if (args[1].contains("<@!") && args[1].contains(">"))
            {
                String tmp = args[1].replace("<@!", "");
                tmp = tmp.replace(">", "");
                id = Long.parseLong(tmp);
            }
        }

        checkUserFile( id , s );
        Player p = PlayerBuilder.load(id , e.getGuild().getName());
        EmbedBuilder eb = new EmbedBuilder();
        String ping = "<@!" + id + ">";
        
        eb.setTitle("Balance");
        eb.addField(new Field("$" + p.getBal(),
                ping + " your current balcance is $" + p.getBal(), true));
        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void work(GuildMessageReceivedEvent e) 
    {
        Server s = ServerBuilder.buildServer(e.getGuild());
        long id = e.getAuthor().getIdLong();

        checkUserFile( id , s );

        Player p = PlayerBuilder.load(e.getAuthor().getIdLong() , e.getGuild().getName());
        for (Player player : cooldownUsers) 
        {
            if (player.equals(p))
            {
                sendTooFast(e);
                return;
            }    
        }

        int num = (int) (Math.random() * this.workOptions.length);
        int bal = (int) (Math.random() * 120 + 30);

        EmbedBuilder eb = new EmbedBuilder();
        eb.addField(new Field("Work", e.getAuthor().getAsMention() + ", " + workOptions[num] + " you make $" + bal, true));
        e.getChannel().sendMessage(eb.build()).queue();
        p.addBal(bal);

        cooldown(e, s.getCooldown());
    }

    private void sendTooFast(GuildMessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField(new Field("Slow Down", "You went a little too fast there, " + e.getAuthor().getAsMention() + ", please slow down", true));
        e.getChannel().sendMessage(eb.build()).queue();;
    }

    private void cooldown(GuildMessageReceivedEvent e, int cooldown) 
    {
        List<Player> cdUsers = this.cooldownUsers;
        Player p = PlayerBuilder.load(e.getAuthor().getIdLong() , e.getGuild().getName());
        Server s = ServerBuilder.buildServer(e.getGuild());

        boolean v1 = e.getMember().getPermissions().contains(Permission.ADMINISTRATOR);
        boolean v2 = s.getAdminCooldownBypass();
        if (v1 && v2)
            return;


        Runnable timer = new Runnable(){
            @Override
            public void run() 
            {
                cdUsers.add(p);
                try {
                    Thread.sleep(cooldown * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cdUsers.remove(p);
            }
        };
        Thread cooldownThread = new Thread(timer, "cooldown " + p.getId());
        cooldownThread.setDaemon(true);
        cooldownThread.start();
    }

    private void checkUserFile(long ID , Server s) 
    {
        String path = s.getPath() + "\\users\\" + ID + ".txt";
        if (Saver.saveNewFile(path))
        {
            String[] out = {
            "" + s.getStartingBalance()
            };

            Saver.saveOverwite(path, out);
        }
    }

    private void help(GuildMessageReceivedEvent e) 
    {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Commands");

        String names =  "work \n " +
                        "bal\n" + 
                        "me\n" + 
                        "buy\n" + 
                        "buildings";

        String functions =  "generates a random amount of money\n" + 
                            "shows your current balance\n" +
                            "shows your balance and your owned buildings\n" +
                            "use to buy a building\n" + 
                            "shows a list of all the buildings";

        eb.addField(new Field("Name", names, true));
        eb.addField(new Field("Functions", functions, true));

        e.getChannel().sendMessage(eb.build()).queue();
    }
    
}
