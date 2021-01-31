package alloy.handler;

import java.util.List;

import alloy.gameobjects.player.Building;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.utility.discord.AlloyUtil;
import io.FileReader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class ViewHandler {

    public static void viewBuildings(Guild g, TextChannel channel, Sendable bot) {
        EmbedBuilder eb = new EmbedBuilder();
        List<Building> buildings = AlloyUtil.loadBuildings(g);

        eb.setTitle("All available buildings");
        embedBuildingList(eb, buildings);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("ViewHandler");
        sm.setMessage(eb.build());
        bot.send(sm);
    }

    private static void embedBuildingList(EmbedBuilder eb, List<Building> buildings) {
        String names = "";
        String costs = "";
        String gener = "";

        for (Building b : buildings) {
            names += "" + b.getName() + "\n";
            costs += "" + b.getCost() + "\n";
            gener += "" + b.getGeneration() + "\n";

        }

        eb.addField("Name", names, true);
        eb.addField("Cost", costs, true);
        eb.addField("Generation", gener, true);
    }

    public static void viewWork(Guild g, TextChannel channel, Sendable bot) {
        EmbedBuilder eb = new EmbedBuilder();
        String path = AlloyUtil.getGuildPath(g) + AlloyUtil.SETTINGS_FOLDER + AlloyUtil.SUB
                + AlloyUtil.WORK_OPTIONS_FILE;
        String[] wO = FileReader.read(path);

        eb.setTitle("All available work options");
        embedWorkArray(eb, wO);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("ViewHandler");
        sm.setMessage(eb.build());
        bot.send(sm);

    }

    private static void embedWorkArray(EmbedBuilder eb, String[] options) {
        String num = "";
        String name = "";

        int i = 1;
        for (String string : options) {
            num += i + "\n";
            name += string + "\n";
            i++;
        }

        eb.addField("#", num, true);
        eb.addField("name", name, true);
    }

}
