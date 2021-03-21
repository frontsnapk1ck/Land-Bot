package frontsnapk1ck.alloy.command.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInput;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.input.Input;
import frontsnapk1ck.input.InputMap;
import frontsnapk1ck.utility.Util;

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

        //CONFIG
        map.put ( "frontsnapk1ck.alloy.command.configuration.SetCommand"              ,       "set"                 );
        map.put ( "frontsnapk1ck.alloy.command.configuration.StartingBalanceCommand"  ,       "starting-balance"    );
        map.put ( "frontsnapk1ck.alloy.command.configuration.XPBlacklistCommand"      ,       "blacklist"           );
        map.put ( "frontsnapk1ck.alloy.command.configuration.BuildingCommand"         ,       "building"            );
        map.put ( "frontsnapk1ck.alloy.command.configuration.CooldownCommand"         ,       "cooldown"            );
        map.put ( "frontsnapk1ck.alloy.command.configuration.PrefixCommand"           ,       "prefix"              );
        map.put ( "frontsnapk1ck.alloy.command.configuration.ViewCommand"             ,       "view"                );
        map.put ( "frontsnapk1ck.alloy.command.configuration.WorkCommand"             ,       "workO"               );
        
        //FUN
        map.put ( "frontsnapk1ck.alloy.command.fun.DeadChatCommand"                   ,       "dead-chat"           );
        map.put ( "frontsnapk1ck.alloy.command.fun.SpamCommand"                       ,       "spam"                );
        map.put ( "frontsnapk1ck.alloy.command.fun.RemindCommand"                     ,       "remindme"            );
        map.put ( "frontsnapk1ck.alloy.command.fun.RankCommand"                       ,       "!rank"               );
        map.put ( "frontsnapk1ck.alloy.command.fun.SayCommand"                        ,       "say"                 );
        map.put ( "frontsnapk1ck.alloy.command.fun.LinkCommand"                       ,       "link"                );
        map.put ( "frontsnapk1ck.alloy.command.fun.HackCommand"                       ,       "hack"                );
        
        //ECON
        map.put ( "frontsnapk1ck.alloy.command.economy.PayCommand"                    ,       "pay"                 );
        map.put ( "frontsnapk1ck.alloy.command.economy.BuyCommand"                    ,       "buy"                 );
        map.put ( "frontsnapk1ck.alloy.command.economy.WorkCommand"                   ,       "work"                );
        map.put ( "frontsnapk1ck.alloy.command.economy.BankCommand"                   ,       "bank"                );
        map.put ( "frontsnapk1ck.alloy.command.economy.MeCommand"                     ,       "me"                  );
        map.put ( "frontsnapk1ck.alloy.command.economy.DayCommand"                    ,       "day"                 );
        
        //ADMIN
        map.put ( "frontsnapk1ck.alloy.command.administration.MuteCommand"            ,       "mute"                );
        map.put ( "frontsnapk1ck.alloy.command.administration.KickCommand"            ,       "kick"                );
        map.put ( "frontsnapk1ck.alloy.command.administration.BanCommand"             ,       "ban"                 );
        map.put ( "frontsnapk1ck.alloy.command.administration.CaseCommand"            ,       "case"                );
        map.put ( "frontsnapk1ck.alloy.command.administration.WarnCommand"            ,       "warn"                );
        map.put ( "frontsnapk1ck.alloy.command.administration.PurgeCommand"           ,       "purge"               );
        map.put ( "frontsnapk1ck.alloy.command.administration.WarningsCommand"        ,       "warnings"            );
        map.put ( "frontsnapk1ck.alloy.command.administration.UnmuteCommand"          ,       "unmute"              );
        
        //CONSOLE
        map.put ( "frontsnapk1ck.alloy.command.console.MembersCommand"                ,       "members"             );
        map.put ( "frontsnapk1ck.alloy.command.console.CacheCommand"                  ,       "cache"               );
        map.put ( "frontsnapk1ck.alloy.command.console.UpdateCommand"                 ,       "update"              );
        map.put ( "frontsnapk1ck.alloy.command.console.RolesCommand"                  ,       "roles"               );
        map.put ( "frontsnapk1ck.alloy.command.console.QueueCommand"                  ,       "queue"               );
        map.put ( "frontsnapk1ck.alloy.command.console.TestCommand"                   ,       "test"                );
        map.put ( "frontsnapk1ck.alloy.command.console.NameCommand"                   ,       "name"                );
        map.put ( "frontsnapk1ck.alloy.command.console.InviteCommand"                 ,       "invite"              );
       
        //INFO
        map.put ( "frontsnapk1ck.alloy.command.info.InviteCommand"                    ,       "invite"              );
        map.put ( "frontsnapk1ck.alloy.command.info.HelpCommand"                      ,       "help"                );
        map.put ( "frontsnapk1ck.alloy.command.info.InfoCommand"                      ,       "info"                );
        map.put ( "frontsnapk1ck.alloy.command.info.PingMeCommand"                    ,       "prefixShow"          );
        map.put ( "frontsnapk1ck.alloy.command.info.PingCommand"                      ,       "ping"                );
        map.put ( "frontsnapk1ck.alloy.command.info.DonateCommand"                    ,       "donate"              );
        map.put ( "frontsnapk1ck.alloy.command.info.UptimeCommand"                    ,       "uptime"              );
        
        //LEVEL
        map.put ( "frontsnapk1ck.alloy.command.level.LeaderboardCommand"              ,       "leaderboard"         );
        map.put ( "frontsnapk1ck.alloy.command.level.RankupCommand"                   ,       "rankup"              );
        map.put ( "frontsnapk1ck.alloy.command.level.RankCommand"                     ,       "rank"                );
       
        //VOICE
        map.put ( "frontsnapk1ck.alloy.command.voice.JoinCommand"                     ,       "join"                );
        map.put ( "frontsnapk1ck.alloy.command.voice.LeaveCommand"                    ,       "leave"               );
        map.put ( "frontsnapk1ck.alloy.command.voice.PlayCommand"                     ,       "play"                );
        map.put ( "frontsnapk1ck.alloy.command.voice.PlayNowCommand"                  ,       "play-now"            );
        map.put ( "frontsnapk1ck.alloy.command.voice.PlayTopCommand"                  ,       "play-top"            );
        map.put ( "frontsnapk1ck.alloy.command.voice.SkipCommand"                     ,       "skip"                );
        map.put ( "frontsnapk1ck.alloy.command.voice.ForceSkipCommnand"               ,       "force-skip"          );
        map.put ( "frontsnapk1ck.alloy.command.voice.QueueCommand"                    ,       "queue"               );
        map.put ( "frontsnapk1ck.alloy.command.voice.NowPlayingCommand"               ,       "now-playing"         );
        map.put ( "frontsnapk1ck.alloy.command.voice.LyricsCommand"                   ,       "lyrics"              );
        map.put ( "frontsnapk1ck.alloy.command.voice.RemoveCommand"                   ,       "remove"              );

        
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
        String name = MAP.get(tmp);
        if (name == null)
            System.out.println(tmp);
        return name;
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