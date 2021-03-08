package alloy.handler.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import alloy.command.util.AbstractCommand;
import alloy.command.util.CommandInfoLoader;
import alloy.utility.discord.AlloyUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import utility.StringUtil;
import utility.Util;

public class HelpHandler {

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
        map.remove("alloy.command.voice");
        
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
    
}
