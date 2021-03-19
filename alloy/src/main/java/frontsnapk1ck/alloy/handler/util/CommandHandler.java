package frontsnapk1ck.alloy.handler.util;

import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.console.ConsoleInput;
import frontsnapk1ck.alloy.input.console.ConsoleInputSystem;
import frontsnapk1ck.alloy.input.discord.AlloyInput;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.input.discord.AlloyInputSystem;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.disterface.util.template.Template;
import frontsnapk1ck.input.ActionMap;
import frontsnapk1ck.input.InputMap;
import frontsnapk1ck.input.InputSystem;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandHandler {

    private static InputSystem system;

    public static boolean isCommand(String msg, String mentionMe, String mentionMeAlias, Server s)
    {
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

    private static void configureSystemConsole() 
    {
        InputMap mapI = AlloyInputUtil.loadConsoleInputMap();
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
        configureSystemConsole();
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

    public static void warnServerNotLoaded(AlloyInput in) 
    {
        AlloyInputData data = in.getData();
        
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        Template t = Templates.botStillLoading();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(CommandHandler.class);
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }   

}
