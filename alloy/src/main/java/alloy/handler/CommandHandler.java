package alloy.handler;

import alloy.gameobjects.Server;
import alloy.input.AlloyInputUtil;
import alloy.input.console.ConsoleInput;
import alloy.input.console.ConsoleInputSystem;
import alloy.input.discord.AlloyInput;
import alloy.input.discord.AlloyInputSystem;
import input.ActionMap;
import input.InputMap;
import input.InputSystem;
import net.dv8tion.jda.api.JDA;

public class CommandHandler {

    private static InputSystem system;

    public static boolean isCommand(String msg, String mentionMe, String mentionMeAlias, Server s) {
        boolean b = msg.startsWith(s.getPrefix()) || msg.startsWith(mentionMe) || msg.startsWith(mentionMeAlias);
        return b;
    }

    private static void configureSystemAlloy() 
    {
        InputMap mapI = AlloyInputUtil.loadInputMap();
        ActionMap mapA = AlloyInputUtil.loadActionMap();

        system = new AlloyInputSystem();
        system.setActionMap(mapA);
        system.setInputMap(mapI);
    }

    private static void configureSystemConsole(JDA jda) 
    {
        InputMap mapI = AlloyInputUtil.loadConsoleInputMap(jda);
        ActionMap mapA = AlloyInputUtil.loadConsoleActionMap();

        system = new ConsoleInputSystem();
        system.setActionMap(mapA);
        system.setInputMap(mapI);
    }

    public static void process(AlloyInput in) 
    {
        configureSystemAlloy();
        system.onInput(in);
    }

    public static void print(String string) 
    {
        System.out.println(string);
	}

    public static void process(ConsoleInput in) 
    {
        configureSystemConsole(in.getJda());
        system.onInput(in);
	}

    public static AlloyInput removePrefix(AlloyInput in, Server s) 
    {
        String prefix = s.getPrefix();
        String message = in.getMessage();
        String trigger = message.replaceFirst(prefix , "" );
        in.setTrigger(trigger);
        return in;
	}   

}
