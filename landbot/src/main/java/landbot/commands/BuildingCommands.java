package landbot.commands;

import java.util.HashMap;
import java.util.List;

import landbot.Server;
import landbot.builder.BuildingBuilder;
import landbot.builder.PlayerBuilder;
import landbot.builder.ServerBuilder;
import landbot.player.Building;
import landbot.player.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.RoleAction;

public class BuildingCommands extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Server s = ServerBuilder.buildServer(e.getGuild());

        if (e.getAuthor().isBot())
            return;

        String[] args = e.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(s.getPrefix() + "buildings"))
            viewBuildings(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "buy"))
            buyBuilding(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "me"))
            showStats(e);
    }

    private void showStats(GuildMessageReceivedEvent e) {
        Player p = PlayerBuilder.load(e.getAuthor().getIdLong(), e.getGuild().getName());

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(e.getAuthor().getAsTag());
        eb.addField(new Field("$" + p.getBal(),
                e.getAuthor().getAsMention() + " your current balcance is $" + p.getBal(), false));
        embedOwned(eb, p);
        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void embedOwned(EmbedBuilder eb, Player p) {
        String name = "";
        String gene = "";
        String numb = "";

        HashMap<String, List<Building>> owned = p.getOwned();
        List<String> keys = p.getTypes();

        for (String s : keys) {
            List<Building> bs = owned.get(s);
            if (bs.size() != 0) {
                Building b = bs.get(0);

                name += b.getName() + "\n";
                gene += "" + b.getGeneration() + "\n";
                numb += "" + bs.size() + "\n";
            }
        }

        eb.addField(new Field("Name", name, true));
        eb.addField(new Field("Generation", gene, true));
        eb.addField(new Field("Quantity", numb, true));

    }

    private void buyBuilding(GuildMessageReceivedEvent e, String[] args) {

        if (args.length == 1) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Buy");
            eb.addField(new Field("Use this command to buy Buildings",
                    "be sure to put the name of the building after the the command", true));
            e.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        long playerID = e.getAuthor().getIdLong();
        Player p = PlayerBuilder.load(playerID, e.getGuild().getName());
        Building b = BuildingBuilder.loadBuilding(args[1], e.getGuild().getName());

        if (b == null) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Invalid Building Name");
            eb.addField(new Field("Please try again", "Be sure to select a builing from the list of buidlings", true));
            e.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        if (p.buyBuilding(b)) {
            String name = b.getName();
            String numType = "" + p.getNumType(b);
            String totalNum = "" + p.getTotalOwned();

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Succsess!");
            eb.addField(new Field("Congradulations on your Purchace",
                    "you have bought a(n) " + name + " bringing your total nubmer of owned " + name + " to " + numType
                            + " and your total number of owned to " + totalNum,
                    true));
            e.getChannel().sendMessage(eb.build()).queue();

            updateRoles(e, b);
        } else {
            String name = b.getName();

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Error!");
            eb.addField(new Field("You could not afford that building",
                    "You could not afford a " + name + " work some more and you will be able to in no time", true));
            e.getChannel().sendMessage(eb.build()).queue();
        }

    }

    private void updateRoles(GuildMessageReceivedEvent e, Building b) 
    {
        if (!ServerBuilder.buildServer(e.getGuild()).getRoleAssignOnBuy())
            return;
        
        checkServerRoles(e, b);
        addRollToUser(e , b);
    }

    private void addRollToUser(GuildMessageReceivedEvent e, Building b) 
    {
        Runnable addRole = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                checkUserRoles(e, b);
            }
        };
        Thread t = new Thread(addRole, "This is stupid, gotta wait for the thing to add a roll to the server");
        t.setDaemon(true);
        t.start();
    }

    private void checkUserRoles(GuildMessageReceivedEvent e, Building b) 
    {
        boolean contains = false;
        List<Role> roles = e.getMember().getRoles();
        for (Role role : roles) {
            if (role.getName().contentEquals(b.getName()))
                contains = true;
        }

        if (!contains)
        {
            Role role = e.getGuild().getRolesByName(b.getName(), true).get(0);
            e.getGuild().addRoleToMember(e.getMember(), role).queue();
        }
    }

    private void checkServerRoles(GuildMessageReceivedEvent e, Building b) 
    {
        boolean contains = false;
        List<Role> roles = e.getGuild().getRoles();

        for (Role role : roles) {
            if (role.getName().equalsIgnoreCase(b.getName()))
                contains = true;
        }

        if (!contains) {
            RoleAction role = e.getGuild().createRole();
            role.setName(b.getName());
            role.queue();
        }
    }

    private void viewBuildings(GuildMessageReceivedEvent e) 
    {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("All availible buildings");
        embedBuildingList(eb, BuildingBuilder.loadBuildings(e.getGuild().getName()));
        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void embedBuildingList ( EmbedBuilder eb , List<Building> buildings )
    {
        String names = "";
        String costs = "";
        String gener = "";

        for (Building b : buildings) 
        {
            names += "" + b.getName() + "\n";
            costs += "" + b.getCost() + "\n";
            gener += "" + b.getGeneration() + "\n";

        }

        eb.addField(new Field( "Name" ,         names , true));
        eb.addField(new Field( "Cost" ,         costs , true));
        eb.addField(new Field( "Generation" ,   gener , true));

    }

}
