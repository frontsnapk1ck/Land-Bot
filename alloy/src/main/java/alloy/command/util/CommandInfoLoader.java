package alloy.command.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInput;
import alloy.main.Alloy;
import alloy.utility.discord.AlloyUtil;
import input.Input;
import input.InputMap;
import utility.Util;

public class CommandInfoLoader {

    public CommandInfoLoader() {
        super();
    }

    public static void loadInfo() {
        Reflections reflections = new Reflections(AlloyUtil.COMMANDS_PATH);
        Set<Class<? extends AbstractCommand>> classes = reflections.getSubTypesOf(AbstractCommand.class);
        filterSet(classes);
        List<AlloyInput> filled = getFilled();

        for (Class<? extends AbstractCommand> c : classes) 
            updateCommand(c, filled);

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

    private static void updateCommand(Class<? extends AbstractCommand> c, List<AlloyInput> filled) 
    {
        try 
        {
            AbstractCommand command = c.getConstructor().newInstance();
            for (AlloyInput in : filled) 
            {
                if (c.getSimpleName().contains(in.getName()))
                {
                    command.setPermission(in.getRequiredPerm());
                    if (!Util.arrayToList(command.getAliases()).contains(in.getTrigger()))
                    {
                        List<String> triggers = Util.arrayToList(command.getAliases());
                        List<String> usages = Util.arrayToList(command.getUsage());
                        triggers.add( in.getTrigger() );
                        usages.add( in.getMessage() + "\t\t\t//" + in.getDescription() );

                        command.setUsage(usages.toArray(new String[] {}));
                        command.setAliases(triggers.toArray(new String[] {}));
                    }
                }
            }

        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) 
        {
            Alloy.LOGGER.warn("CommandInfoLoader" , e.toString());
        }
    }

    private static List<AlloyInput> getFilled() 
    {
        List<AlloyInput> out = new ArrayList<AlloyInput>();
        InputMap map = AlloyInputUtil.loadInputMap();
        List<Input> oInputs = map.getInputs();
        for (Input input : oInputs)
            out.add((AlloyInput) input);
        return out;
    }

}