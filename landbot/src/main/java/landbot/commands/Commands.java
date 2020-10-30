package landbot.commands;

import java.util.ArrayList;
import java.util.List;

import landbot.builder.loaders.PlayerLoaderText;
import landbot.builder.loaders.ServerLoaderText;
import landbot.gameobjects.Server;
import landbot.gameobjects.player.Player;
import landbot.io.FileReader;
import landbot.utility.PlayerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Commands extends PlayerCommand {

    String[] workOptions;
    private List<Player> cooldownUsers;

    public Commands() {
        this.cooldownUsers = new ArrayList<Player>();
    }

    private void createWorkOptions(Server s) {
        this.workOptions = FileReader.read(s.getPath() + "\\settings\\work.options");
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) 
    {
        super.onGuildMessageReceived(e);
        if (e.getAuthor().isBot())
            return;

        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        createWorkOptions(s);

        String[] args = e.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(s.getPrefix() + "work"))
            work(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "bal"))
            checkBalance(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "help"))
            help(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "!rank"))
            rank(e);
        
        else if (args[0].equalsIgnoreCase(s.getPrefix() + "dead-chat"))
            deadchat(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "say"))
            say(e);
        
        else if (args[0].equalsIgnoreCase(s.getPrefix() + "invite"))
            invite(e);
        
        else if (args[0].equalsIgnoreCase(s.getPrefix() + "info"))
            info(e);

    }

    private void info(GuildMessageReceivedEvent e) 
    {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setFooter("frontsnapk1ck", "https://cdn.discordapp.com/avatars/312743142828933130/7c63b41c5ed601b3314c1dce0d0e0065.png");
        eb.setTitle("My name is alloy");
        eb.setDescription("I am Alloy and i am a got frontsnapk1ck has been working on for a little while now. if you have any question feel free to reach out to him and join the offical Alloy Support Server here https://discord.gg/7UNxyXRxBh \n\nthanks!");

        e.getChannel().sendMessage(eb.build()).queue();        
    }

    private void invite(GuildMessageReceivedEvent e) 
    {
        String inviteLink = "https://discord.com/api/oauth2/authorize?client_id=762825892006854676&permissions=435678326&scope=bot";
        String rickroll = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Click here to invite me", rickroll);
        eb.setDescription("thanks for thinking of me");
        e.getChannel().sendMessage(eb.build()).queue();

        eb.setTitle("This is your actual invite");
        eb.setDescription("my creator will never miss an opertunity to rick roll someone\n\n" + inviteLink);
        PrivateChannel c = e.getAuthor().openPrivateChannel().complete();
        c.sendMessage(eb.build()).queue();

    }

    private void deadchat(GuildMessageReceivedEvent e) 
    {        
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        long id = e.getAuthor().getIdLong();

        PlayerLoaderText plt = new PlayerLoaderText();
        String path = getGuildPath(e.getGuild()) + "\\users\\" + id;
        Player p = plt.load(path);

        for (Player player : cooldownUsers) 
        {
            if (player.equals(p)) 
            {
                sendTooFast(e);
                return;
            }
        }

        String[] chats = {
            "https://tenor.com/view/dead-chat-xd-gif-18749255" , 
            "https://tenor.com/view/dead-chat-dead-discord-death-gif-18239566" ,
            "https://tenor.com/view/dead-chat-gif-18800792"
        };

        int i = (int) (Math.random() * chats.length);
        String rankMessage = chats[i];

        e.getMessage().delete().queue();

        e.getChannel().sendMessage(rankMessage).queue();
        cooldown(e, s.getWorkCooldown());
    }

    private void say(GuildMessageReceivedEvent e) {

        if (e.getAuthor().getIdLong() == 312743142828933130l) {
            String out = e.getMessage().getContentRaw().toString();
            out = out.substring(5);
            e.getMessage().delete().queue();
            e.getChannel().sendMessage(out).queue();
        }

        else if (e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            EmbedBuilder em = new EmbedBuilder();
            em.setFooter(e.getAuthor().getName(), e.getAuthor().getAvatarUrl());
            em.setTitle("Announcement");
            String out = e.getMessage().getContentRaw().toString();
            out = out.substring(5);
            em.setDescription(out);
            e.getChannel().sendMessage(em.build()).queue();
        }
    }

    private void rank(GuildMessageReceivedEvent e) {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        long id = e.getAuthor().getIdLong();

        PlayerLoaderText plt = new PlayerLoaderText();
        String path = getGuildPath(e.getGuild()) + "\\users\\" + id;
        Player p = plt.load(path);

        for (Player player : cooldownUsers) {
            if (player.equals(p)) {
                sendTooFast(e);
                return;
            }
        }

        String[] ranks = { 
                "https://tenor.com/view/rank-talk-selfie-man-eyeglasses-gif-17817029",
                "https://tenor.com/view/rank-hair-long-weird-beat-box-gif-17161979",
                "https://tenor.com/view/rank-funny-face-black-man-gif-18421232",
                "https://tenor.com/view/rank-conner-when-rank-hedoberankindoe-gif-18818063",
                "https://tenor.com/view/timotainment-tim-entertainment-rank-discord-gif-18070842" 
            };

        int i = (int) (Math.random() * ranks.length);
        String rankMessage = ranks[i];

        e.getChannel().sendMessage(rankMessage).queue();
        cooldown(e, s.getWorkCooldown());
    }

    private void checkBalance(GuildMessageReceivedEvent e, String[] args) 
    {
        long id = e.getMember().getIdLong();

        if (args.length != 1) {
            if (args[1].contains("<@!") && args[1].contains(">")) {
                String tmp = args[1].replace("<@!", "");
                tmp = tmp.replace(">", "");
                id = Long.parseLong(tmp);
            }
        }

        PlayerLoaderText plt = new PlayerLoaderText();
        String path = getGuildPath(e.getGuild()) + "\\users\\" + id;
        Player p = plt.load(path);
        EmbedBuilder eb = new EmbedBuilder();
        String ping = "<@!" + id + ">";

        eb.setTitle("Balance");
        eb.addField(new Field("$" + p.getBal(), ping + " your current balcance is $" + p.getBal(), true));
        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void work(GuildMessageReceivedEvent e) {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        long id = e.getAuthor().getIdLong();

        PlayerLoaderText plt = new PlayerLoaderText();
        String path = getGuildPath(e.getGuild()) + "\\users\\" + id;
        Player p = plt.load(path);
        for (Player player : cooldownUsers) {
            if (player.equals(p)) {
                sendTooFast(e);
                return;
            }
        }

        int num = (int) (Math.random() * this.workOptions.length);
        int bal = (int) (Math.random() * 120 + 30);

        EmbedBuilder eb = new EmbedBuilder();
        eb.addField(
                new Field("Work", e.getAuthor().getAsMention() + ", " + workOptions[num] + " you make $" + bal, true));
        e.getChannel().sendMessage(eb.build()).queue();
        p.addBal(bal);

        cooldown(e, s.getWorkCooldown());
    }

    private void sendTooFast(GuildMessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField(new Field("Slow Down",
                "You went a little too fast there, " + e.getAuthor().getAsMention() + ", please slow down", true));
        e.getChannel().sendMessage(eb.build()).queue();
        ;
    }

    private void cooldown(GuildMessageReceivedEvent e, int cooldown) {
        Long id = e.getAuthor().getIdLong();
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        List<Player> cdUsers = this.cooldownUsers;

        PlayerLoaderText plt = new PlayerLoaderText();
        String path = getGuildPath(e.getGuild()) + "\\users\\" + id;
        Player p = plt.load(path);

        boolean valid = e.getMember().getPermissions().contains(Permission.ADMINISTRATOR) && s.getAdminCooldownBypass();
        if (valid)
            return;

        Runnable timer = new Runnable() {
            @Override
            public void run() {
                cdUsers.add(p);
                try {
                    Thread.sleep(cooldown * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cdUsers.remove(p);
            }
        };
        Thread cooldownThread = new Thread(timer, "Work cooldown " + e.getAuthor().getAsTag());
        cooldownThread.setDaemon(true);
        cooldownThread.start();
    }

    @Override
    protected void help(GuildMessageReceivedEvent e) 
    {
        PrivateChannel c = e.getAuthor().openPrivateChannel().complete();
        EmbedBuilder eb = new EmbedBuilder();
        String message = loadHelpMessage();
        eb.setTitle("Commands");
        eb.setDescription(message);

        c.sendMessage(eb.build()).queue();
        e.getChannel().sendMessage("DM sent");
    }

    private String loadHelpMessage() 
    {
        String out = "";
        String[] message = FileReader.read("landbot\\res\\globals\\help\\help.msg");
        for (String string : message)
            out += string + "\n";
        return out;
    }
    
}
