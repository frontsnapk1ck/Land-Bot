package alloy.input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import alloy.input.actions.AdministrationInputActions;
import alloy.input.actions.ConfigurationInputActions;
import alloy.input.actions.ConsoleInputActions;
import alloy.input.actions.EconomyInputActions;
import alloy.input.actions.FunInputActions;
import alloy.input.actions.InfoInputActions;
import alloy.input.actions.LevelInputActions;
import alloy.input.builder.AlloyInputMapBuilder;
import alloy.input.builder.ConsoleInputMapBuilder;
import alloy.input.discord.AlloyInputData;
import alloy.input.discord.AlloyInputEvent;
import alloy.utility.discord.AlloyUtil;
import input.ActionMap;
import input.InputMap;

public class AlloyInputUtil {

    public static InputMap loadInputMap() 
    {
        File xmlFile = new File(AlloyUtil.COMMANDS_FILE);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            Element e = doc.getDocumentElement();

            InputMap inputMap = AlloyInputMapBuilder.load(e);
            return inputMap;
        } catch (ParserConfigurationException | SAXException | IOException e) 
        {
            e.printStackTrace();
        }
        return null;
    }

    public static ActionMap loadActionMap() 
    {
		ActionMap map = new ActionMap();

        map.put(    "purge"             ,   AdministrationInputActions.PURGE_ACTION);
        map.put(    "ban"               ,   AdministrationInputActions.BAN_ACTION);
        map.put(    "case"              ,   AdministrationInputActions.CASE_ACTION);
        map.put(    "kick"              ,   AdministrationInputActions.KICK_ACTION);
        map.put(    "unmute"            ,   AdministrationInputActions.UNMUTE_ACTION);
        map.put(    "warn"              ,   AdministrationInputActions.WARN_ACTION);
        map.put(    "mute"              ,   AdministrationInputActions.MUTE_ACTION);
        map.put(    "warnings"          ,   AdministrationInputActions.WARNINGS_ACTION);

        map.put(    "building"          ,   ConfigurationInputActions.BUILDING_ACTION);
        map.put(    "workO"             ,   ConfigurationInputActions.WORK_ACTION);
        map.put(    "view"              ,   ConfigurationInputActions.VIEW_ACTION);
        map.put(    "starting-balance"  ,   ConfigurationInputActions.STARTING_BALANCE_ACTION);
        map.put(    "cooldown"          ,   ConfigurationInputActions.COOLDOWN_ACTION);
        map.put(    "prefix"            ,   ConfigurationInputActions.PREFIX_ACTION);
        map.put(    "blacklist"         ,   ConfigurationInputActions.XP_BLACKLIST_ACTION);
        map.put(    "set"               ,   ConfigurationInputActions.SET_ACTION);
    
        map.put(    "bank"              ,   EconomyInputActions.BANK_ACTION);
        map.put(    "buy"               ,   EconomyInputActions.BUY_ACTION);
        map.put(    "pay"               ,   EconomyInputActions.PAY_ACTION);
        map.put(    "day"               ,   EconomyInputActions.DAY_ACTION);
        map.put(    "me"                ,   EconomyInputActions.ME_ACTION);
        map.put(    "work"              ,   EconomyInputActions.WORK_ACTION);
    
        map.put(    "dead-chat"         ,   FunInputActions.DEAD_CHAT_ACTION);
        map.put(    "link"              ,   FunInputActions.LINK_ACTION);
        map.put(    "!rank"             ,   FunInputActions.RANK_ACTION);
        map.put(    "remindme"          ,   FunInputActions.REMIND_ACTION);
        map.put(    "say"               ,   FunInputActions.SAY_ACTION);
        map.put(    "spam"              ,   FunInputActions.SPAM_ACTION);
        map.put(    "hack"              ,   FunInputActions.HACK_ACTION);
    
        map.put(    "donate"            ,   InfoInputActions.DONATE_ACTION);
        map.put(    "help"              ,   InfoInputActions.HELP_ACTION);
        map.put(    "invite"            ,   InfoInputActions.INVITE_ACTION);
        map.put(    "ping"              ,   InfoInputActions.PING_ACTION);
        map.put(    "prefixShow"        ,   InfoInputActions.PREFIX_SHOW_ACTION);
        map.put(    "info"              ,   InfoInputActions.INFO_ACTION);
        map.put(    "uptime"            ,   InfoInputActions.UPTIME_ACTION);

        map.put(    "rank"              ,   LevelInputActions.RANK_ACTION);
        map.put(    "rankup"            ,   LevelInputActions.RANKUP_ACTION);
        map.put(    "leaderboard"       ,   LevelInputActions.LEADERBOARD_ACTION);

        return map;
	}

    public static String[] getArgs(AlloyInputEvent e) 
    {
        String command = e.getMessage();
        String[] allArgs = command.split(" ");
        String[] newArgs = new String[allArgs.length -1 ];
        for (int i = 1; i < allArgs.length; i++) 
            newArgs[i-1] = allArgs[i];
        
        List<String> finalArgs = new ArrayList<String>();
        for (String string : newArgs) 
        {
            if (!string.equals(""))
                finalArgs.add(string);
        }

        return finalArgs.toArray(new String[]{});
	}

    public static String[] getArgs(AlloyInputData data) 
    {
		return getArgs(data.getEvent());
	}

    public static InputMap loadConsoleInputMap()
    {
        File xmlFile = new File(AlloyUtil.CONSOLE_COMMANDS_FILE);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            Element e = doc.getDocumentElement();

            InputMap inputMap = ConsoleInputMapBuilder.load(e);
            return inputMap;
        } catch (ParserConfigurationException | SAXException | IOException e) 
        {
            e.printStackTrace();
        }
        return null;
    }

    public static ActionMap loadConsoleActionMap()
    {
        ActionMap map = new ActionMap();

        map.put(    "invite"        ,   ConsoleInputActions.GET_INVITES_ACTION);
        map.put(    "members"       ,   ConsoleInputActions.MEMBERS_ACTION);
        map.put(    "name"          ,   ConsoleInputActions.NAME_ACTION);
        map.put(    "roles"         ,   ConsoleInputActions.ROLES_ACTION);
        map.put(    "queue"         ,   ConsoleInputActions.QUEUE_ACTION);
        map.put(    "test"          ,   ConsoleInputActions.TEST_ACTION);
        map.put(    "cache"         ,   ConsoleInputActions.CACHE_ACTION);
        map.put(    "update"        ,   ConsoleInputActions.UPDATE_ACTION);
        map.put(    "dm"            ,   ConsoleInputActions.DM_ACTION);
        
        return map;
    }
    
}
