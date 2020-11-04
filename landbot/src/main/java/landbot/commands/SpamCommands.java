package landbot.commands;

import java.util.ArrayList;
import java.util.List;

import landbot.builder.loaders.ServerLoaderText;
import landbot.gameobjects.Server;
import landbot.utility.AlloyCommandListener;
import landbot.utility.event.SpamFinishEvent;
import landbot.utility.event.SpamFinishListener;
import landbot.utility.timer.SpamRunnable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SpamCommands extends AlloyCommandListener implements SpamFinishListener {

    private List<SpamRunnable> spams;

    public SpamCommands() 
    {
        super(SpamCommands.class.getName());
        spams = new ArrayList<SpamRunnable>();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) 
    {
        super.onGuildMessageReceived(e);
        if (e.getAuthor().isBot())
            return;

        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        String[] args = e.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(s.getPrefix() + "spam"))
            spam(e, args);
        else if (args[0].equalsIgnoreCase(s.getPrefix() + "stop-spam"))
            stopSpam(e, args);
    }

    private void stopSpam(GuildMessageReceivedEvent e, String[] args) {
        if (!validID(args))
            return;

        Long l = Long.parseLong(args[1]);
        for (SpamRunnable s : this.spams) {
            if (s.getID().equals(l)) {
                s.stop();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setFooter(e.getAuthor().getAsTag(), e.getAuthor().getAvatarUrl());
                eb.setTitle("This spam with the id `" + l + "` has stopped");
                e.getChannel().sendMessage(eb.build()).queue();
            }
        }
    }

    private boolean validID(String[] args) {
        try 
        {
            Long.parseLong(args[1]);
            return true;
        } catch (NumberFormatException e) 
        {
        }
        return false;
    }

    private void spam(GuildMessageReceivedEvent e, String[] args) {
        SpamRunnable r = makeRunnable(e, args);
        Thread t = new Thread(r, "Spam Manager");
        t.setDaemon(true);
        t.start();
    }

    private SpamRunnable makeRunnable(GuildMessageReceivedEvent e, String[] args) {
        Long num = getRandomNumber();

        if (validCommand(e, args)) {
            int reps = getReps(e, args);
            String message = buildMessage(args);
            TextChannel c = getChannel(e);

            SpamRunnable r = new SpamRunnable(reps, message, c);
            r.addListener(this);
            r.setID(num);
            this.spams.add(r);

            e.getChannel().sendMessage(buildIDSend(e, num)).queue();

            return r;
        }
        return null;
    }

    private MessageEmbed buildIDSend(GuildMessageReceivedEvent e, Long num) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setFooter(e.getAuthor().getAsTag(), e.getAuthor().getAvatarUrl());
        eb.setTitle("ID to stop this spam");
        eb.setDescription("to stop this spam, use the command \n`!stop-spam " + num + "`");
        return eb.build();
    }

    private TextChannel getChannel(GuildMessageReceivedEvent e) {
        ServerLoaderText slt = new ServerLoaderText();
        Server s = slt.load(getGuildPath(e.getGuild()));
        List<GuildChannel> channels = e.getGuild().getChannels();

        long spamID = s.getSpamChannel();
        for (GuildChannel c : channels) {
            if (c.getId().contentEquals("" + spamID))
                return (TextChannel) c;
        }
        return null;
    }

    private String buildMessage(String[] args) {
        String message = "";
        for (int i = 2; i < args.length; i++)
            message += args[i] + " ";
        return message;
    }

    private int getReps(GuildMessageReceivedEvent e, String[] args) {
        int num = Integer.parseInt(args[1]);
        if (!e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)
                && e.getAuthor().getIdLong() != 312743142828933130l)
            num = num > 5 ? 5 : num;
        else if (e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)
                && e.getAuthor().getIdLong() != 312743142828933130l)
            num = num > 60 ? 60 : num;
        return num;

    }

    private boolean validCommand(GuildMessageReceivedEvent e, String[] args) {
        boolean valid = false;
        valid = args.length >= 2;
        if (!valid)
            return false;

        try {
            int num = Integer.parseInt(args[1]);
            if (num < 1)
                return false;
        } catch (NumberFormatException ex) {
            e.getChannel().sendMessage("please enter a number");
            return false;
        }
        return true;
    }

    private Long getRandomNumber() {
        boolean valid = false;
        Long l = 0l;

        while (!valid) {
            l = (long) (Math.random() * 1000000000);
            if (spams.size() == 0)
                valid = true;
            for (SpamRunnable r : spams)
                valid = (r.getID() == l) ? false : true;
        }
        return l;
    }

    @Override
    public void onSpamFinishEvent(SpamFinishEvent e) 
    {
        this.spams.remove(e.getRunnable());
    }
}