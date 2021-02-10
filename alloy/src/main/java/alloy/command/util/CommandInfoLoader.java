package alloy.command.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInput;
import alloy.main.Alloy;
import input.Input;
import input.InputMap;
import utility.Util;

public class CommandInfoLoader {

    public static final Map<String , String> MAP;

    static{
        MAP = configMap();
    }

    public CommandInfoLoader() {
        super();
    }

    private static Map<String, String> configMap() 
    {
        Map<String,String> map  = new HashMap<String, String>();

        map.put ( "alloy.command.configuration.SetCommand"              ,       "set"      );
        map.put ( "alloy.command.configuration.StartingBalanceCommand"  ,       "starting-balance"      );
        map.put ( "alloy.command.configuration.XPBlacklistCommand"      ,       "blacklist"      );
        map.put ( "alloy.command.configuration.BuildingCommand"         ,       "building"      );
        map.put ( "alloy.command.configuration.CooldownCommand"         ,       "cooldown"      );
        map.put ( "alloy.command.configuration.PrefixCommand"           ,       "prefix"      );
        map.put ( "alloy.command.configuration.ViewCommand"             ,       "view"      );
        map.put ( "alloy.command.configuration.WorkCommand"             ,       "workO"      );
        
        map.put ( "alloy.command.fun.DeadChatCommand"                   ,       "dead-chat"      );
        map.put ( "alloy.command.fun.SpamCommand"                       ,       "spam"      );
        map.put ( "alloy.command.fun.RemindCommand"                     ,       "remindme"      );
        map.put ( "alloy.command.fun.RankCommand"                       ,       "!rank"      );
        map.put ( "alloy.command.fun.SayCommand"                        ,       "say"      );
        map.put ( "alloy.command.fun.LinkCommand"                       ,       "link"      );
        map.put ( "alloy.command.fun.HackCommand"                       ,       "hack"      );
        
        map.put ( "alloy.command.economy.PayCommand"                    ,       "pay"      );
        map.put ( "alloy.command.economy.BuyCommand"                    ,       "buy"      );
        map.put ( "alloy.command.economy.WorkCommand"                   ,       "work"      );
        map.put ( "alloy.command.economy.BankCommand"                   ,       "bank"      );
        map.put ( "alloy.command.economy.MeCommand"                     ,       "me"      );
        map.put ( "alloy.command.economy.DayCommand"                    ,       "day"      );
        
        map.put ( "alloy.command.administration.MuteCommand"            ,       "mute"      );
        map.put ( "alloy.command.administration.KickCommand"            ,       "kick"      );
        map.put ( "alloy.command.administration.BanCommand"             ,       "ban"      );
        map.put ( "alloy.command.administration.CaseCommand"            ,       "case"      );
        map.put ( "alloy.command.administration.WarnCommand"            ,       "warn"      );
        map.put ( "alloy.command.administration.PurgeCommand"           ,       "purge"      );
        map.put ( "alloy.command.administration.WarningsCommand"        ,       "warnings"      );
        map.put ( "alloy.command.administration.UnmuteCommand"          ,       "unmute"      );
        
        map.put ( "alloy.command.console.MembersCommand"                ,       "members"      );
        map.put ( "alloy.command.console.CacheCommand"                  ,       "cache"      );
        map.put ( "alloy.command.console.UpdateCommand"                 ,       "update"      );
        map.put ( "alloy.command.console.RolesCommand"                  ,       "roles"      );
        map.put ( "alloy.command.console.QueueCommand"                  ,       "queue"      );
        map.put ( "alloy.command.console.TestCommand"                   ,       "test"      );
        map.put ( "alloy.command.console.NameCommand"                   ,       "name"      );
        map.put ( "alloy.command.console.InviteCommand"                 ,       "invite"      );
       
        map.put ( "alloy.command.info.InviteCommand"                    ,       "invite"      );
        map.put ( "alloy.command.info.HelpCommand"                      ,       "help"      );
        map.put ( "alloy.command.info.InfoCommand"                      ,       "info"      );
        map.put ( "alloy.command.info.PingMeCommand"                    ,       "prefixShow"      );
        map.put ( "alloy.command.info.PingCommand"                      ,       "ping"      );
        map.put ( "alloy.command.info.DonateCommand"                    ,       "donate"      );
        map.put ( "alloy.command.info.UptimeCommand"                    ,       "uptime"      );
        
        map.put ( "alloy.command.level.LeaderboardCommand"              ,       "leaderboard"      );
        map.put ( "alloy.command.level.RankupCommand"                   ,       "rankup"      );
        map.put ( "alloy.command.level.RankCommand"                     ,       "rank"      );
       
        map.put ( "alloy.command.voice.JoinCommand"                     ,       "TODO"      );
        map.put ( "alloy.command.voice.LeaveCommand"                    ,       "TODO"      );
        
        return map;
    }

    public static AbstractCommand getData(Class<? extends AbstractCommand> c)
    {
        List<AlloyInput> filled = getFilled();
        String name = getName(c);

        List<AlloyInput> inputs = new ArrayList<AlloyInput>();
        for (AlloyInput in : filled) 
        {
            if (in.getName().equalsIgnoreCase(name))
                inputs.add(in);
        }
        return fill(c,inputs);
    }

    private static String getName(Class<? extends AbstractCommand> c) 
    {
        String tmp = c.getName();
        return MAP.get(tmp);
    }

    private static AbstractCommand fill(Class<? extends AbstractCommand> c, List<AlloyInput> inputs) 
    {
        try 
        {
            AbstractCommand command = c.getConstructor().newInstance();
            for (AlloyInput in : inputs) 
            {
                command.setPermission(in.getRequiredPerm());
                String[] usage = command.getUsageActual();
                List<String> usages = Util.arrayToList(usage);
                usages.add( in.getTrigger() + "\t//\t" + in.getDescription() );
                String[] triggers = command.getAliases();
                triggers = updateTriggers(in,triggers);

                command.setAliases(triggers);
                command.setUsage(usages.toArray(new String[]{}));
            }
            return command;
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) 
        {
            Alloy.LOGGER.warn("CommandInfoLoader" , e.toString());
            return null;
        }
    }

    private static String[] updateTriggers(AlloyInput in, String[] triggers) 
    {
        List<String> strings = Util.arrayToList(triggers);
        if (!strings.contains(in.getTrigger().split(" ")[0]))
            strings.add(in.getTrigger().split(" ")[0]);
        return strings.toArray(new String[]{});
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