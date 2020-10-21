package landbot.commands;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import landbot.Server;
import landbot.builder.BuildingBuilder;
import landbot.builder.PlayerBuilder;
import landbot.builder.ServerBuilder;
import landbot.io.FileReader;
import landbot.io.Saver;
import landbot.player.Building;
import landbot.player.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AdminCommands extends ListenerAdapter {

    private String[] building;
    private boolean buildingB;
    private User buildingConstrucort;
    private boolean removeBuilding;
    private String guildname;
    private boolean removeWorkOption;

    public AdminCommands() {
        Runnable day = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(24 * // hours
                        60 * // mins
                        60 * // secs
                        1000); // milis

                    }

                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    day();
                }
            }
        };

        Thread t = new Thread(day, "Day Timer");
        t.setDaemon(true);
        t.start();
    }

    protected void day() {
        List<Player> players = PlayerBuilder.getAllPlayers(this.guildname);
        for (Player p : players)
            p.day();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if (e.getAuthor().isBot())
            return;

        String[] args = e.getMessage().getContentRaw().split(" ");
        Server s = ServerBuilder.buildServer(e.getGuild());
        this.guildname = e.getGuild().getName();

        if (args[0].equalsIgnoreCase(s.getPrefix() + "prefix"))
            prefixCommand(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "starting-balance"))
            startingBalanceCommand(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "cooldown"))
            cooldown(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "reset-buildings"))
            resetBuildings(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "add-building"))
            addBuilding(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "rm-building"))
            removeBuilding(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "reset-work"))
            resetWorkOptions(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "add-work"))
            addWorkOption(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "rm-work"))
            removeWorkOption(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "view-work"))
            viewWorkOptions(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "day"))
            day(e);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "buy-roles"))
            buyRoles(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "admin-cooldown"))
            adminCooldown(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "spam-channel"))
            setSpam(e, args);

        else if (args[0].equalsIgnoreCase(s.getPrefix() + "help"))
            help(e);

        else if ((this.removeBuilding || this.buildingB || this.removeWorkOption)
                && e.getAuthor().equals(this.buildingConstrucort) && args[0].contains("cancel"))
            cancelProgress(e);

        else if (this.removeBuilding && e.getAuthor().equals(this.buildingConstrucort))
            selectRemoveBuilding(e);

        else if (this.buildingB && e.getAuthor().equals(this.buildingConstrucort))
            buildBuilding(args[0], e);

        else if (this.removeWorkOption && e.getAuthor().equals(this.buildingConstrucort))
            selectWorkOptionRemove(e);

        else
            cancelProgress(e);

    }

    private void setSpam(GuildMessageReceivedEvent e, String[] args) {
        cancelProgress(e);
        Server s = ServerBuilder.buildServer(e.getGuild());

        if (!hasPerm(e, Permission.ADMINISTRATOR, true))
            return;

        if (args.length == 1) {
            e.getChannel().sendTyping().queue();
            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle("Spam Channel");
            eb.addField(new Field("The Spam Channel is Currently", "<#" + s.getSpamChannel() + ">", true));
            e.getChannel().sendMessage(eb.build()).queue();
        }

        else {
            String spam = args[1];
            spam = spam.replace("<#", "");
            spam = spam.replace(">", "");
            boolean valid = false;
            List<GuildChannel> channels = e.getGuild().getChannels();
            for (GuildChannel c : channels) 
            {
                if (c.getId().equals(spam))
                    valid = true;
            }

            if (valid)
            {
                s.changeSpamChannel(Long.parseLong(spam));
                EmbedBuilder eb = new EmbedBuilder();

                eb.setTitle("Spam Channel");
                eb.addField(new Field("The Spam Channel is now", "<#" + s.getSpamChannel() + ">", true));
                e.getChannel().sendMessage(eb.build()).queue();
            }
            else
                e.getChannel().sendMessage("Please enter a valid channel").queue();;
        }
    }

    private void adminCooldown(GuildMessageReceivedEvent e, String[] args) 
    {
        cancelProgress(e);
        Server s = ServerBuilder.buildServer(e.getGuild());

        if (!hasPerm(e, Permission.ADMINISTRATOR, true))
            return;

        if (args.length == 1) {
            e.getChannel().sendTyping().queue();
            EmbedBuilder eb = new EmbedBuilder();

            String state = s.getAdminCooldownBypass() ? "on" : "off"; 

            eb.setTitle("Admin Cooldown Bypass");
            eb.addField(new Field("The Admin Bypass Cooldown is Currently", "`" + state + "`", true));
            e.getChannel().sendMessage(eb.build()).queue();
        }

        else
        {
            boolean valid = false;
            if (!valid) {
                try {
                    boolean b = Boolean.parseBoolean(args[1]);
                    valid = true;
                    s.changeAdminCooldownBypass(b);
                }

                catch (NumberFormatException err) {
                    e.getChannel().sendMessage("Please enter `true` pr `false`");
                }
            }

            EmbedBuilder eb = new EmbedBuilder();

            String state = s.getAdminCooldownBypass() ? "on" : "off"; 

            eb.setTitle("Admin Cooldown Bypass");
            eb.addField(new Field("The Admin Bypass Cooldown is Currently", "`" + state + "`", true));
            e.getChannel().sendMessage(eb.build()).queue();
        }
    }

    private void selectWorkOptionRemove(GuildMessageReceivedEvent e) 
    {
        try {
            int rm = Integer.parseInt(e.getMessage().getContentRaw());
            Server s = ServerBuilder.buildServer(e.getGuild());
            String path = s.getPath() + "\\settings\\work.options";
            String[] options = FileReader.read(path);


            if (rm > 0 && rm <= options.length) 
            {
                options[rm-1] = null;
                String[] newOp = new String[options.length -1 ];

                int i = 0;
                for (String st : options) 
                {
                    if (st != null)
                    {
                        newOp[i] = st;
                        i++;
                    }
                }
                Saver.saveOverwite( path , newOp );
            } 
            else
                e.getChannel().sendMessage("Please enter a valid number");

            this.viewWorkOptions(e);
            this.removeWorkOption = false;

        } catch (NumberFormatException ex) {
            e.getChannel().sendMessage("Please enter and Integer").queue();
        }
    }

    private void removeWorkOption(GuildMessageReceivedEvent e) 
    {
        cancelProgress(e);
        if (!hasPerm(e, Permission.ADMINISTRATOR , true))
            return;

        this.buildingConstrucort = e.getAuthor();
        this.removeWorkOption = true;
        viewWorkOptions(e);

    }

    private void resetWorkOptions(GuildMessageReceivedEvent e) 
    {
        Server s = ServerBuilder.buildServer(e.getGuild());

        cancelProgress(e);
        if (!hasPerm(e, Permission.ADMINISTRATOR , true))
            return;
        
        String[] out = {
            "you work at mcdonalds" ,
            "you work at burger king" ,
            "you work at walmart" 
        };

        String path = s.getPath() + "\\settings\\work.options";
        Saver.saveOverwite(path, out);
        viewWorkOptions(e);
    }

    private void viewWorkOptions(GuildMessageReceivedEvent e) 
    {
        cancelProgress(e);
        if (!hasPerm(e, Permission.ADMINISTRATOR, true))
            return;

        Server s = ServerBuilder.buildServer(e.getGuild());

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("All availible work options");
    
        String path = s.getPath() + "\\settings\\work.options";
        String[] wO = FileReader.read(path);

        String num = "";
        String name = "";

        int i = 1;
        for (String string : wO) 
        {
            num += i + "\n";
            name += string + "\n";
            i++;
        }

        eb.addField(new Field("#", num, true));
        eb.addField(new Field("name", name, true));

        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void addWorkOption(GuildMessageReceivedEvent e, String[] args) 
    {
        cancelProgress(e);
        if (!hasPerm(e, Permission.ADMINISTRATOR, true))
            return;

        Server s = ServerBuilder.buildServer(e.getGuild());
        String out = "";
        for (int i = 1; i < args.length; i++) 
            out += args[i] + " ";
        
        String path = s.getPath() + "\\settings\\work.options";
        Saver.saveAppend(path, out);
    }

    private void buyRoles(GuildMessageReceivedEvent e, String[] args) 
    {
        cancelProgress(e);
        Server s = ServerBuilder.buildServer(e.getGuild());

        if (!hasPerm(e, Permission.ADMINISTRATOR, true))
            return;

        if (args.length == 1) {
            e.getChannel().sendTyping().queue();
            EmbedBuilder eb = new EmbedBuilder();

            String state = s.getRoleAssignOnBuy() ? "on" : "off"; 

            eb.setTitle("Auto Assign Roles");
            eb.addField(new Field("The Auto Assign Roles is Currently", "`" + state + "`", true));
            e.getChannel().sendMessage(eb.build()).queue();
        }

        else
        {
            boolean valid = false;
            while (!valid) {
                try {
                    boolean b = Boolean.parseBoolean(args[1]);
                    valid = true;
                    s.changeAssignRolesOnBuy(b);
                }

                catch (NumberFormatException err) {
                    e.getChannel().sendMessage("Please enter `true` pr `false`");
                }
            }

            EmbedBuilder eb = new EmbedBuilder();

            String state = s.getRoleAssignOnBuy() ? "on" : "off"; 

            eb.setTitle("Auto Assign Roles");
            eb.addField(new Field("The Auto Assign Roles is Now", "`" + state + "`", true));
            e.getChannel().sendMessage(eb.build()).queue();
        }
    }

    private void cooldown(GuildMessageReceivedEvent e, String[] args) {
        cancelProgress(e);
        Server s = ServerBuilder.buildServer(e.getGuild());
        if (args.length == 1) {
            e.getChannel().sendTyping().queue();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Cooldown");
            eb.addField(new Field("The Cooldown is Currently", "" + s.getCooldown(), true));
            e.getChannel().sendMessage(eb.build()).queue();
        }

        else {
            if (!hasPerm(e, Permission.ADMINISTRATOR, true))
                return;

            e.getChannel().sendTyping().queue();
            boolean valid = false;
            while (!valid) {
                try {
                    int newCooldown = Integer.parseInt(args[1]);
                    valid = true;
                    s.changeCooldown(newCooldown);
                }

                catch (NumberFormatException err) {
                    e.getChannel().sendMessage("Please enter an integer");
                }
            }

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Cooldown");
            eb.addField(new Field("The Cooldown is Now", "" + s.getCooldown(), true));
            e.getChannel().sendMessage(eb.build()).queue();
        }
    }

    private void help(GuildMessageReceivedEvent e) {
        if (!hasPerm(e, Permission.ADMINISTRATOR, false))
            return;

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Admin Only Commands");

        String names =  "Prefix \n " + 
                        "cooldown\n" + 
                        "rm-Buildings\n" + 
                        "reset-buildings\n" + 
                        "add-building\n" + 
                        "reset-work\n" + 
                        "add-work\n" + 
                        "rm-work\n" +
                        "view-work\n" +
                        "day\n" +
                        "buy-roles\n" + 
                        "admin-cooldown\n" + 
                        "help";

        String functions =  "Change the prfix for this bot\n" + 
                            "Change the defualt cooldown for the work command\n"+ 
                            "remove a building\n" + 
                            "resets all buildings and refunds the purchaser\n" + 
                            "adds a building to the economy\n" + 
                            "resets the work options to default\n" + 
                            "adds an option to the work command\n" + 
                            "removes an options form the work commands\n" + 
                            "shows all the optinos for the `work` command\n" + 
                            "increments the day paying out all building owner - use sparingly\n" + 
                            "toggles giving roles to users when they buy buildings\n" + 
                            "toggles admins bypassing the cooldown\n" + 
                            "shows this message";

        eb.addField(new Field("Name", names, true));
        eb.addField(new Field("Functions", functions, true));

        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void day(GuildMessageReceivedEvent e) {
        if (!hasPerm(e, Permission.ADMINISTRATOR, true))
            return;

        List<Player> players = PlayerBuilder.getAllPlayers(e.getGuild().getName());
        for (Player p : players)
            p.day();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Sucsess");
        eb.addField(new Field("The Day Has Adveanced", "the day has scsessfully advanced", true));
        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void cancelProgress(GuildMessageReceivedEvent e) 
    {
        boolean sendMessage = this.buildingB || this.removeBuilding;
        this.buildingB = false;
        this.building = null;
        this.removeBuilding = false;
        this.buildingConstrucort = null;
        this.removeWorkOption = false;

        if (sendMessage)
            cancelEmbed(e);
    }

    private void startingBalanceCommand(GuildMessageReceivedEvent e, String[] args) {
        cancelProgress(e);
        Server s = ServerBuilder.buildServer(e.getGuild());

        if (!hasPerm(e, Permission.ADMINISTRATOR, true))
            return;

        if (args.length == 1) {
            e.getChannel().sendTyping().queue();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Starting Balance");
            eb.addField(new Field("The Starting Balance is Currently $", "" + s.getStartingBalance(), true));
            e.getChannel().sendMessage(eb.build()).queue();
        }

        else {
            e.getChannel().sendTyping().queue();
            s.changePrefix(args[1]);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Prefix");
            eb.addField(new Field("The Prefix is Now", s.getPrefix(), true));
            e.getChannel().sendMessage(eb.build()).queue();
        }
    }

    private void prefixCommand(GuildMessageReceivedEvent e, String[] args) {
        cancelProgress(e);
        Server s = ServerBuilder.buildServer(e.getGuild());

        if (args.length == 1) {
            e.getChannel().sendTyping().queue();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Prefix");
            eb.addField(new Field("The Prefix is Currently", s.getPrefix(), true));
            e.getChannel().sendMessage(eb.build()).queue();
        }

        else {
            if (!hasPerm(e, Permission.ADMINISTRATOR, true))
                return;

            e.getChannel().sendTyping().queue();
            s.changePrefix(args[1]);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Prefix");
            eb.addField(new Field("The Prefix is Now", s.getPrefix(), true));
            e.getChannel().sendMessage(eb.build()).queue();
        }
    }

    private boolean hasPerm(GuildMessageReceivedEvent e, Permission p, boolean b) {
        EnumSet<Permission> authorRoles = e.getMember().getPermissions();
        boolean valid = authorRoles.contains(p); // || e.getAuthor().getIdLong() == 312743142828933130l;
        if (!valid && b) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Insufficent Permisions");
            eb.addField(new Field("you can only use this command if you have the `" + p.getName() + "` permission", "",
                    true));
            e.getChannel().sendMessage(eb.build()).queue();
        }
        return valid;
    }

    private void selectRemoveBuilding(GuildMessageReceivedEvent e) {
        try {
            List<Building> buildings = BuildingBuilder.loadBuildings(e.getGuild().getName());
            int rm = Integer.parseInt(e.getMessage().getContentRaw());
            Building b;
            Server s = ServerBuilder.buildServer(e.getGuild());

            if (rm > 0 && rm <= buildings.size()) {
                b = buildings.remove(rm - 1);
                this.removeBuildingFromPlayers(PlayerBuilder.getAllPlayers(e.getGuild().getName()), b, e);
            } else
                e.getChannel().sendMessage("Please enter a valid number");

            this.writeBuildings(organizeBuildings(buildings), s);
            this.viewBuildings(e);
            this.removeBuilding = false;

        } catch (NumberFormatException ex) {
            e.getChannel().sendMessage("Please enter and Integer").queue();
        }
    }

    private void removeBuildingFromPlayers(List<Player> users, Building b, GuildMessageReceivedEvent e) 
    {
        Role roll = e.getGuild().getRolesByName(b.getName(), true).get(0);

        for (Player p : users) 
        {
            p.removeBuilding(b);
            e.getGuild().removeRoleFromMember(e.getMember() , roll).queue();
        }

        roll.delete();
    }

    private List<Building> organizeBuildings(List<Building> buildings) 
    {
        Collections.sort(buildings);
        return buildings;
    }

    private void removeBuilding(GuildMessageReceivedEvent e) 
    {
        cancelProgress(e);
        if (!hasPerm(e, Permission.ADMINISTRATOR , true))
            return;
        
        this.removeBuilding = true;
        this.buildingConstrucort = e.getAuthor();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Choose Building to remove");
        embedBuildingListNumbered( eb , BuildingBuilder.loadBuildings(e.getGuild().getName()) );
        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void cancelEmbed(GuildMessageReceivedEvent e) 
    {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Cancel");
        eb.addField( new Field("Current progress has been cancelled ", "", true));
        e.getChannel().sendMessage(eb.build()).queue();
	}

	private void buildBuilding(String args, GuildMessageReceivedEvent e) 
    {
        Server s = ServerBuilder.buildServer(e.getGuild());

        if (this.building[0] == "")
            this.building[0] = args;
        
        else if ( this.building[1] == "" )
        {
            try 
            {
                this.building[1] = "" + Integer.parseInt(args);
            } 
            catch (Exception ex) 
            {
                e.getChannel().sendTyping().queue();
                e.getChannel().sendMessage("Please enter and Integer").queue();
            }
        }
        else if ( this.building[2] == "" )
        {
            try 
            {
                this.building[2] = "" + Integer.parseInt(args);
            } 
            catch (Exception ex) 
            {
                e.getChannel().sendTyping().queue();
                e.getChannel().sendMessage("Please enter and Integer").queue();
            }
        }

        boolean valid = this.building[0] != "" &&
                        this.building[1] != "" &&
                        this.building[2] != "";

        if (valid)
        {
            if (ableAddBuilding(e))
            {
                writeBuilding(s);
                writeBuildings( organizeBuildings(e) , s );
                embedCurrentBuilding(e);
                this.building = null;
                this.buildingB = false;
                this.buildingConstrucort = null;
                return;
            }
            else 
            {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Invalid");
                eb.addField(new Field("Too long", "what youve entered has made the list of building too long. please enter a shorter name or remove some buildings", true));
                e.getChannel().sendMessage(eb.build()).queue();
            }
        }

        embedCurrentBuilding(e);

    }

    private void writeBuilding(Server s) 
    {
        String save = BuildingBuilder.loadBuilding(this.building).toString();
        String path = s.getPath() + "\\settings\\buildings.txt";

        Saver.saveAppend(path, save);
    }

    private void writeBuildings(List<Building> list , Server s) 
    {
        String path = s.getPath() + "\\settings\\buildings.txt";
        String[] arr = new String[list.size()];
        for (int i = 0; i < arr.length; i++) 
            arr[i] = list.get(i).toString();
        
        Saver.saveOverwite(path, arr);

    }

    private List<Building> organizeBuildings(GuildMessageReceivedEvent e) 
    {
        List<Building> buildings = BuildingBuilder.loadBuildings(e.getGuild().getName());
        return this.organizeBuildings(buildings);
    }

    private void addBuilding(GuildMessageReceivedEvent e) 
    {
        cancelProgress(e);
        if (!hasPerm(e, Permission.ADMINISTRATOR , true))
            return;

        this.buildingB = true;
        this.building = new String[] {"","",""};
        this.buildingConstrucort = e.getAuthor();
        embedCurrentBuilding(e);
    }
    
    private boolean ableAddBuilding(GuildMessageReceivedEvent e) 
    {
        String names = "";
        String costs = "";
        String gener = "";
        List<Building> buildings = BuildingBuilder.loadBuildings(e.getGuild().getName());
        buildings.add (BuildingBuilder.loadBuilding(this.building));
        
        for (Building b : buildings) 
        {
            names += "" + b.getName() + "\n";
            costs += "" + b.getCost() + "\n";
            gener += "" + b.getGeneration() + "\n";
        }
        
        boolean valid = names.length() <= 1024 &&
                        costs.length() <= 1024 &&
                        gener.length() <= 1024;
        
        return valid;
    }

    private void resetBuildings(GuildMessageReceivedEvent e) 
    {
        Server s = ServerBuilder.buildServer(e.getGuild());

        cancelProgress(e);
        if (!hasPerm(e, Permission.ADMINISTRATOR , true))
            return;
        
        List<Building> bs = BuildingBuilder.loadBuildings(e.getGuild().getName());
        List<Player> players = PlayerBuilder.getAllPlayers(e.getGuild().getName());

        for (Building b : bs) 
        {
            for (Player p : players)
                p.removeBuilding(b);
            List<Role> rolls = e.getGuild().getRolesByName(b.getName(), true);
            if (rolls.size() > 0)
                rolls.get(0).delete().queue();
        }

        e.getChannel().sendTyping().queue();
        String[] out =  {  "shack:2500:15" ,
                            "apartment:7500:50" ,
                            "townhome:14500:110" ,
                            "bungalow:18000:175" ,
                            "ranch style house:23500:215" ,
                            "cottage:25750:250" ,
                            "cabin:29500:300" ,
                            "chalet:35000:375" ,
                            "carriage house:42000:500" ,
                            "craftsman home:57500:750" ,
                            "mansion:69000:1000" ,
                            "contemporary mansion:100000:1500" ,
                            "castle:150000:2000"    };
        
        String path = s.getPath() + "\\settings\\buildings.txt";
        Saver.saveOverwite(path, out);
        viewBuildings(e);
    }

    private void embedCurrentBuilding (GuildMessageReceivedEvent e)
    {
        e.getChannel().sendTyping().queue();
        EmbedBuilder eb = new EmbedBuilder();

        if (this.building[2] != "")
            eb.setTitle("Added new Building");
        else
            eb.setTitle("Add new Building");

        eb.addField(new Field("Name",       this.building[0] , true));
        eb.addField(new Field("Cost",       this.building[1] , true));
        eb.addField(new Field("Generation", this.building[2] , true));
        e.getChannel().sendMessage(eb.build()).queue();

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

    private void embedBuildingListNumbered ( EmbedBuilder eb , List<Building> buildings )
    {
        String numbr = "";
        String names = "";
        
        int i = 1;
        for (Building b : buildings) 
        {
            names += "" + b.getName() + "\n";
            numbr += "" + i + "\n";
            i++;
        }

        eb.addField(new Field( "#" ,    numbr , true));
        eb.addField(new Field( "Name" , names , true));
    }

}
