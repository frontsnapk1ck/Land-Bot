package frontsnapk1ck.alloy.handler.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.command.util.CommandInfoLoader;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import frontsnapk1ck.utility.StringUtil;
import frontsnapk1ck.utility.Util;

public class InfoHandler {

    public static final int MAX_PREFIX_LENGTH = 4;

    public static List<MessageEmbed> loadHelp() 
    {
		Reflections reflections = new Reflections(AlloyUtil.COMMANDS_PATH);
        Set<Class<? extends AbstractCommand>> classes = reflections.getSubTypesOf(AbstractCommand.class);
        filterSet(classes);
        List<MessageEmbed> messages = new ArrayList<MessageEmbed>();
        Map<String, List<Class<? extends AbstractCommand>>> map = loadMap(classes);
        filterMap(map);
        
        Set<String> packages = map.keySet();
        for (String string : packages) 
            messages.addAll(helpMessage(map.get(string)));
        
        return messages;
    }
    
    private static void filterMap(Map<String, List<Class<? extends AbstractCommand>>> map) 
    {
        map.remove("alloy.command.console");
        
    }

    private static List<MessageEmbed> helpMessage(List<Class<? extends AbstractCommand>> list) 
    {
        List<MessageEmbed> embeds = new ArrayList<MessageEmbed>();

        EmbedBuilder eb = new EmbedBuilder();
        
        List<String> outs = new ArrayList<String>();
        outs.add("");

        for (Class<? extends AbstractCommand> c : list) 
        {
            String currentOut = outs.get(outs.size() - 1);

            String newC = "**" + c.getSimpleName().replace("Command", "") + "**\n";
            AbstractCommand command = CommandInfoLoader.getData(c);
            String[] usage = command.getUsageActual();
            String[] triggers = command.getAliases();
            
            String triggersSingle = "Aliases: " + StringUtil.list(triggers);
            String usageSingle = "\nUsage\n";
            for (String string : usage) 
                usageSingle += string + "\n";
            
            newC += triggersSingle;
            newC += "\n";
            newC += usageSingle;
            newC += "\n";

            
            String tmp = currentOut + newC;
            if (tmp.length() >= MessageEmbed.TEXT_MAX_LENGTH)
                outs.add(newC);
            else
            {
                outs.remove(outs.size() - 1);
                outs.add(tmp);

            }
        }
        for (String string : outs) 
        {
            eb.setTitle(list.get(0).getPackageName().replace("alloy.command.", "").toUpperCase());
            eb.setDescription(string);
            embeds.add(eb.build());
        }

        return embeds;
    }

    private static Map<String, List<Class<? extends AbstractCommand>>> loadMap(
            Set<Class<? extends AbstractCommand>> classes) 
    {
        Map<String, List<Class<? extends AbstractCommand>>> map = new HashMap<String, List<Class<? extends AbstractCommand>>>();
        for (Class<? extends AbstractCommand> c : classes) 
        {
            String name = c.getPackageName();
            if (!map.containsKey(name))
                map.put(name , getAllInPackage(name,classes));
        }
        return map;
        
    }

    private static List<Class<? extends AbstractCommand>> getAllInPackage(String name, Set<Class<? extends AbstractCommand>> classes) 
    {
        List<Class<? extends AbstractCommand>> toReturn = new ArrayList<Class<? extends AbstractCommand>>();
        for (Class<? extends AbstractCommand> c : classes) 
        {
            if (c.getPackageName().equals(name))
                toReturn.add(c);
        }
        return toReturn;
    }

    private static void filterSet(Set<Class<? extends AbstractCommand>> classes) 
    {
        List<Class<? extends AbstractCommand>> toRM = new ArrayList<Class<? extends AbstractCommand>>();
        for (Class<? extends AbstractCommand> c : classes) 
        {
            if (!Util.isInstantiable(c))
                toRM.add(c);
        }
        classes.removeAll(toRM);
    }

    public static boolean changePrefix(Guild g, String prefix) {
        Server s = AlloyUtil.loadServer(g);
        if (validPrefix(prefix))
            s.changePrefix(prefix);
        return validPrefix(prefix);
    }

    private static boolean validPrefix(String prefix) {
        return prefix.length() <= MAX_PREFIX_LENGTH;
    }

    public static void viewPrefix(TextChannel tc, Sendable bot, Guild g) {
        Server s = AlloyUtil.loadServer(g);
        String prefix = s.getPrefix();

        AlloyTemplate t = Templates.prefixIs(prefix);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(tc);
        sm.setFrom(InfoHandler.class);
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }
    
}
