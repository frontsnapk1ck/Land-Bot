package alloy.command.configuration;

import java.util.List;

import alloy.command.util.AbstractCommand;
import alloy.gameobjects.player.Building;
import alloy.handler.BuildingHandler;
import alloy.handler.ViewHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import alloy.utility.settings.BuildingSettings;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.StringUtil;

public class BuildingCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() {
        return DisPerm.ADMINISTRATOR;
    }

    @Override
    public void execute(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);

        if (!DisPermUtil.checkPermission(m, getPermission())) {
            Template t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("BuildingCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args.length == 0) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("BuildingCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args[0].equalsIgnoreCase("add"))
            addBuilding(data);
        else if (args[0].equalsIgnoreCase("remove"))
            removeBuilding(data);
        else if (args[0].equalsIgnoreCase("reset"))
            resetBuilding(data);
    }

    private void addBuilding(AlloyInputData data) {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (args.length < 4) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("BuildingCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        int cost = -1;
        int generation = -1;
        String name = StringUtil.joinStrings(args, 3);

        try {
            cost = Integer.parseInt(args[1]);
            generation = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            Template t = Templates.invalidNumberFormat(args);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("BuildingCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!BuildingHandler.validName(name)) {
            Template t = Templates.invalidBuildingName(name);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("BuildingCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        List<Building> buildings = AlloyUtil.loadBuildings(g);
        BuildingSettings settings = new BuildingSettings();
        settings.setCost(cost).setGeneration(generation).setName(name);

        Building b = new Building(settings);

        if (BuildingHandler.nameOutOfBounds(b, buildings)) {
            Template t = Templates.buildingsNameOutOfBounds(b);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("BuildingCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        BuildingHandler.saveBuilding(g, b);
        buildings = AlloyUtil.loadBuildings(g);

        Template t = Templates.buildingSaveSuccess(buildings);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("BuildingCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);

    }

    private void removeBuilding(AlloyInputData data) {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (args.length < 2) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("BuildingCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        try {
            int i = Integer.parseInt(args[1]);
            Building b = BuildingHandler.removeBuilding(i - 1, g);
            Template t = Templates.buildingsRemoveSuccess(b);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("BuildingCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        } catch (NumberFormatException e) {
            Template t = Templates.invalidNumberFormat(args);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("BuildingCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        } catch (IndexOutOfBoundsException e) {
            Template t = Templates.numberOutOfBounds(e);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("BuildingCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        ViewHandler.viewBuildings(g, channel, bot);

    }

    private void resetBuilding(AlloyInputData data) {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        BuildingHandler.removeAllBuildings(g);
        BuildingHandler.copyOverBuildings(g);

        ViewHandler.viewBuildings(g, channel, bot);
    }

}
