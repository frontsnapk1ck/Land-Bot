package alloy.command.administration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import alloy.command.util.AbstractCommand;
import alloy.gameobjects.Server;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Queueable;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import alloy.utility.job.PurgeJob;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.StringUtil;
import utility.TimeUtil;
import utility.Util;
import utility.event.Job;


public class PurgeCommand extends AbstractCommand {

    public static final int MAX_DELETE_COUNT = 2500;
    public static final int MAX_BULK_SIZE = 100;

    @Override
    public String getDescription() {
        return "deletes non-pinned messages";
    }

    @Override
    public String getCommand() {
        return "purge";
    }

    @Override
    public String[] getUsage() {
        return new String[] { 
                "//deletes up to " + MAX_BULK_SIZE + " non-pinned messages", 
                "purge",

                "//deletes <limit> (max " + MAX_DELETE_COUNT + ") non-pinned messages", 
                "purge <limit>",

                "//deletes messages newer than now - (input)",
                "purge time 1d2h10m         //you can use dhms and combinations ",

                "//deletes <limit> messages from <user>, limit is optional", 
                "purge @user [limit]",

                "//deletes messages from <user>, user can be part of a user's name", 
                "purge user <user>",

                "//deletes messages matching <regex>", 
                "purge matches <regex>",

                "//delete messages NOT matching <regex>", 
                "purge notmatches <regex>",

                "//delete command related messages", 
                "purge commands", 
                
                "//deletes bot messages", 
                "purge bot" 
            };
    }

    @Override
    public String[] getAliases() {
        return new String[] { "clear", "delete" };
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild guild = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Queueable q = data.getQueue();

        boolean hasManageMessages = DisPermUtil.checkPermission(guild.getSelfMember(), DisPerm.MESSAGE_MANAGE);
        List<Message> messagesToDelete = new ArrayList<>();
        Member toDeleteFrom = null;
        String deletePattern = null;
        long maxMessageAge = TimeUnit.DAYS.toMillis(14);
        int toDelete = 100;
        PurgeStyle style = PurgeStyle.UNKNOWN;

        boolean valid = DisPermUtil.checkPermission(guild.getMember(author), DisPerm.ADMINISTRATOR) || channel.getJDA().getSelfUser().equals(author);
        if (!valid) 
        {
            Template t = Templates.noPermission(DisPerm.ADMINISTRATOR, author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("PurgeCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args.length == 0)
            style = PurgeStyle.ALL;
        else if (   args[0].equalsIgnoreCase("time")    )
            maxMessageAge = Math.min(maxMessageAge , TimeUtil.toMillis(StringUtil.joinStrings(args,1)));
        else if (   args[0].equalsIgnoreCase(   "commands"  )   ||
                    args[0].equalsIgnoreCase(   "command"   )       )
            style = PurgeStyle.COMMANDS;

        else if (   args[0].equalsIgnoreCase(   "bot"   )   ||
                    args[0].equalsIgnoreCase(   "bots"  )   ||
                    args[0].equalsIgnoreCase(   "alloy" )       )
            style = PurgeStyle.BOTS;
        else if (   args[0].equalsIgnoreCase(   "user"  )    )
        {
            style = PurgeStyle.USER;

        }
        else if (   args[0].equalsIgnoreCase(   "match"     ) ||
                    args[0].equalsIgnoreCase(   "matches"   ) )
        {
            style = PurgeStyle.MATCHES;
            deletePattern = StringUtil.joinStrings(args, 1);
        }
        else if (   args[0].equalsIgnoreCase(   "notmatch"      ) ||
                    args[0].equalsIgnoreCase(   "notmatches"    )  )
        {
            style = PurgeStyle.NOTMATCHES;
            deletePattern = StringUtil.joinStrings(args, 1);
        }
        else
            style = PurgeStyle.UNKNOWN;
            
        int finalDeleteLimit = getToDelete(toDelete , style , args );
        Member finalToDeleteFrom = toDeleteFrom;
        PurgeStyle finalStyle = style;
        String finalDeletePattern = deletePattern;
        int totalMessages = toDelete;
        MessageHistory history = channel.getHistory();
        int deletedCount = 0;
        
        int part = Math.min(MAX_BULK_SIZE, totalMessages);
        while (part != 0) 
        {
            deletePart( totalMessages , deletedCount , finalDeleteLimit , finalStyle , 
                        hasManageMessages, messagesToDelete , maxMessageAge , history , 
                        finalDeletePattern , finalToDeleteFrom , part );
            totalMessages -= part;
            part = Math.min(MAX_BULK_SIZE, totalMessages);
        }
        deleteBulk(bot, (TextChannel) channel, hasManageMessages, messagesToDelete , q);
        
    }

    private int getToDelete(int toDelete, PurgeStyle style, String[] args) 
    {
        if (style == PurgeStyle.ALL)
        {
            if (args.length > 0 && Util.validInt(args[0]))
                return Util.parseInt(args[0], toDelete) + 1;
        }

        else if (style == PurgeStyle.BOTS)
            return toDelete;

        else if (style == PurgeStyle.USER)
        {
            if (args.length > 1 && Util.validInt(args[1]))
                return Util.parseInt(args[1], toDelete);
        }

        else if (style == PurgeStyle.COMMANDS)
            return toDelete;

        else if (style == PurgeStyle.MATCHES)
            return toDelete;

        else if (style == PurgeStyle.NOTMATCHES)
            return toDelete;

        else if (style == PurgeStyle.UNKNOWN)
        {
            if (args.length > 0 && Util.validInt(args[0]))
                return Util.parseInt(args[0], toDelete) + 1;
        }
        return 0;
    }

    private boolean deletePart(int totalMessages, int deletedCount, int finalDeleteLimit, PurgeStyle finalStyle,
            boolean hasManageMessages, List<Message> messagesToDelete, long maxMessageAge, MessageHistory history,
            String finalDeletePattern, Member finalToDeleteFrom, int part) 
    {
        List<Message> messages = history.retrievePast(part).complete();
        long twoWeeksAgo = (System.currentTimeMillis() - maxMessageAge - net.dv8tion.jda.api.utils.TimeUtil.DISCORD_EPOCH) << net.dv8tion.jda.api.utils.TimeUtil.TIMESTAMP_OFFSET;
        
        String cmdPrefix = "";

        if (messages.size() > 0 )
        {
            Guild g = messages.get(0).getGuild();
            Server s = AlloyUtil.loadServer(g);
            cmdPrefix = s.getPrefix();    
        }

        for (Message msg : messages) 
        {
            if (deletedCount == finalDeleteLimit)
                return false;
            
            if (msg.isPinned() || msg.getIdLong() < twoWeeksAgo )
                return true;

            boolean all = false;
            boolean bot = false;
            boolean user = false;
            boolean command = false;
            boolean matches = false;
            boolean notmatches = false;

            try {
                all = (hasManageMessages || (msg.getAuthor() != null && msg.getAuthor().getId().equals(msg.getJDA().getSelfUser().getId())));
                bot = (hasManageMessages && msg.getAuthor() != null && msg.getAuthor().isBot()) || msg.getAuthor() != null && msg.getAuthor().isBot();
                user = finalToDeleteFrom != null && msg.getAuthor() != null && msg.getAuthor().getId().equals(finalToDeleteFrom.getUser().getId());
                command = (msg.getContentRaw().startsWith(cmdPrefix) && hasManageMessages) || (msg.getAuthor() == null || msg.getAuthor().getId().equals(msg.getJDA().getSelfUser().getId()));
                matches = hasManageMessages && msg.getContentRaw().contains(finalDeletePattern);
                notmatches = hasManageMessages && !msg.getContentRaw().contains(finalDeletePattern);
            } catch (Exception ignored) {
            }

            if (finalStyle == PurgeStyle.ALL && all)
            {
                messagesToDelete.add(msg);
                deletedCount++;
            }

            else if (finalStyle == PurgeStyle.BOTS && bot)
            {
                messagesToDelete.add(msg);
                deletedCount++;
            }

            else if (finalStyle == PurgeStyle.USER && user)
            {
                messagesToDelete.add(msg);
                deletedCount++;
            }

            else if (finalStyle == PurgeStyle.COMMANDS && command)
            {
                messagesToDelete.add(msg);
                deletedCount++;
            }

            else if (finalStyle == PurgeStyle.MATCHES && matches)
            {
                messagesToDelete.add(msg);
                deletedCount++;
            }

            else if (finalStyle == PurgeStyle.NOTMATCHES && notmatches)
            {
                messagesToDelete.add(msg);
                deletedCount++;
            }

            else if (finalStyle == PurgeStyle.UNKNOWN)
            {
                messagesToDelete.add(msg);
                deletedCount++;
            }
            
        }
        return false;
    }

    private void deleteBulk(Sendable bot, TextChannel channel, boolean hasManageMessages, List<Message> messagesToDelete,
            Queueable q) 
    {
        if (messagesToDelete.isEmpty())
            return;
        
        if (hasManageMessages) 
        {
            Template t = Templates.bulkDeleteSucsessfull(channel , messagesToDelete.size() - 1);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("PurgeCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            Job j = new PurgeJob(messagesToDelete , channel);
            q.queue(j);
        }
    }

    private enum PurgeStyle 
    {
        UNKNOWN, ALL, BOTS, USER, MATCHES, NOTMATCHES, COMMANDS
    }
}
