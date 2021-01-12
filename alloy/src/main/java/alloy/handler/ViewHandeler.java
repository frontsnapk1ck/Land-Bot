package alloy.handler;

import java.util.List;

import alloy.gameobjects.player.Building;
import alloy.io.loader.BuildingLoaderText;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.utility.discord.AlloyUtil;
import io.FileReader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class ViewHandeler {

    public static void viewBuildings(Guild g, TextChannel channel, Sendable bot) 
    {
        EmbedBuilder eb = new EmbedBuilder();
        BuildingLoaderText blt = new BuildingLoaderText();
        String bPath = AlloyUtil.getGuildPath(g) + AlloyUtil.SUB + AlloyUtil.SETTINGS_FOLDER + AlloyUtil.SUB + AlloyUtil.BUILDING_FILE;
        List<Building> buildings = blt.loadALl(bPath);

        eb.setTitle("All availible buildings");
        embedBuildingList(eb, buildings );
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("ViewHandeler");
        sm.setMessage(eb.build());
        bot.send(sm);
	}

    private static void embedBuildingList(EmbedBuilder eb, List<Building> buildings) 
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

        eb.addField( "Name" ,         names , true);
        eb.addField( "Cost" ,         costs , true);
        eb.addField( "Generation" ,   gener , true);
    }

    public static void viewWork(Guild g, TextChannel channel, Sendable bot) 
    {
        EmbedBuilder eb = new EmbedBuilder();
        String path = AlloyUtil.getGuildPath(g) + AlloyUtil.SETTINGS_FOLDER + AlloyUtil.SUB + AlloyUtil.WORK_OPTIONS_FILE;
        String[] wO = FileReader.read(path);

        eb.setTitle("All availible work options");
        embedWorkArray(eb, wO);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("ViewHandeler");
        sm.setMessage(eb.build());
        bot.send(sm);

    }
    
    private static void embedWorkArray( EmbedBuilder eb , String[] options )
    {
        String num = "";
        String name = "";

        int i = 1;
        for ( String string : options ) 
        {
            num += i + "\n";
            name += string + "\n";
            i++;
        }

        eb.addField("#", num, true);
        eb.addField("name", name, true);
    }
    
}
