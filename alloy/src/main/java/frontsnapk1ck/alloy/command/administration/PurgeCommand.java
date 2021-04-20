package frontsnapk1ck.alloy.command.administration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import frontsnapk1ck.alloy.command.util.AbstractCooldownCommand;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.main.intefs.Queueable;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.intefs.handler.CooldownHandler;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import frontsnapk1ck.alloy.utility.job.jobs.DelayJob;
import frontsnapk1ck.alloy.utility.job.jobs.DeleteMessageJob;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import frontsnapk1ck.utility.StringUtil;
import frontsnapk1ck.utility.Util;
import frontsnapk1ck.utility.event.Job;
import frontsnapk1ck.utility.time.TimeUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class PurgeCommand extends AbstractCooldownCommand {

    public static final int MAX_DELETE_COUNT = 2500;
    public static final int MAX_BULK_SIZE = 100;

    @Override
    public long getCooldownTime(Guild g) 
    {
        return 20l;
    }

    @Override
    public DisPerm getPermission() 
    {
        return DisPerm.MANAGER;
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        Consumer<AlloyInputData> consumer = new Consumer<AlloyInputData>()
        {
            @Override
            public void accept(AlloyInputData t) 
            {
                exeImp(t);
            }
        };
        DelayJob<AlloyInputData> j = new DelayJob<AlloyInputData>(consumer, data);
        data.getQueue().queue(j);
    }

    private void exeImp(AlloyInputData data) 
    {
        
        Guild guild = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        CooldownHandler handler = data.getCooldownHandler();
        Queueable q = data.getQueue();
        Member m = guild.getMember(author);

        boolean hasManageMessages = DisPermUtil.checkPermission(guild.getSelfMember(), getPermission());
        List<Message> messagesToDelete = new ArrayList<>();
        Member toDeleteFrom = null;
        String deletePattern = null;
        long maxMessageAge = TimeUnit.DAYS.toMillis(14);
        int toDelete = 100;
        PurgeStyle style = PurgeStyle.UNKNOWN;

        boolean valid = DisPermUtil.checkPermission(guild.getMember(author), getPermission())
                || channel.getJDA().getSelfUser().equals(author);
        if (!valid) 
        {
            AlloyTemplate t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (userOnCooldown(author, guild, handler))
        {
            AlloyTemplate t = Templates.onCooldown(m);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);  
            return;
        }

        if (args.length == 0)
            style = PurgeStyle.ALL;
        else if (args[0].equalsIgnoreCase("time"))
            maxMessageAge = Math.min(maxMessageAge, TimeUtil.toMillis(StringUtil.joinStrings(args, 1)));
        else if (args[0].equalsIgnoreCase("commands") || args[0].equalsIgnoreCase("command"))
            style = PurgeStyle.COMMANDS;

        else if (args[0].equalsIgnoreCase("bot") || args[0].equalsIgnoreCase("bots")
                || args[0].equalsIgnoreCase("alloy"))
            style = PurgeStyle.BOTS;
        else if (args[0].equalsIgnoreCase("user")) {
            style = PurgeStyle.USER;

        } else if (args[0].equalsIgnoreCase("match") || args[0].equalsIgnoreCase("matches")) {
            style = PurgeStyle.MATCHES;
            deletePattern = StringUtil.joinStrings(args, 1);
        } else if (args[0].equalsIgnoreCase("notmatch") || args[0].equalsIgnoreCase("notmatches")) {
            style = PurgeStyle.NOTMATCHES;
            deletePattern = StringUtil.joinStrings(args, 1);
        } else
            style = PurgeStyle.UNKNOWN;

        int finalDeleteLimit = getToDelete(toDelete, style, args);
        Member finalToDeleteFrom = toDeleteFrom;
        PurgeStyle finalStyle = style;
        String finalDeletePattern = deletePattern;
        int totalMessages = toDelete;
        MessageHistory history = channel.getHistory();
        int deletedCount = 0;

        int part = Math.min(MAX_BULK_SIZE, totalMessages);
        while (part != 0) {
            deletePart(totalMessages, deletedCount, finalDeleteLimit, finalStyle, hasManageMessages, messagesToDelete,
                    maxMessageAge, history, finalDeletePattern, finalToDeleteFrom, part);
            totalMessages -= part;
            part = Math.min(MAX_BULK_SIZE, totalMessages);
        }
        deleteBulk(bot, (TextChannel) channel, hasManageMessages, messagesToDelete, q);
        addUserCooldown(m, guild, handler, getCooldownTime(guild) , q);
    }

    private int getToDelete(int toDelete, PurgeStyle style, String[] args) {
        if (style == PurgeStyle.ALL) {
            if (args.length > 0 && Util.validInt(args[0]))
                return Util.parseInt(args[0], toDelete) + 1;
        }

        else if (style == PurgeStyle.BOTS)
            return toDelete;

        else if (style == PurgeStyle.USER) {
            if (args.length > 1 && Util.validInt(args[1]))
                return Util.parseInt(args[1], toDelete);
        }

        else if (style == PurgeStyle.COMMANDS)
            return toDelete;

        else if (style == PurgeStyle.MATCHES)
            return toDelete;

        else if (style == PurgeStyle.NOTMATCHES)
            return toDelete;

        else if (style == PurgeStyle.UNKNOWN) {
            if (args.length > 0 && Util.validInt(args[0]))
                return Util.parseInt(args[0], toDelete) + 1;
        }
        return 0;
    }

    private boolean deletePart(int totalMessages, int deletedCount, int finalDeleteLimit, PurgeStyle finalStyle,
            boolean hasManageMessages, List<Message> messagesToDelete, long maxMessageAge, MessageHistory history,
            String finalDeletePattern, Member finalToDeleteFrom, int part) {
        List<Message> messages = history.retrievePast(part).complete();
        long twoWeeksAgo = (System.currentTimeMillis() - maxMessageAge
                - net.dv8tion.jda.api.utils.TimeUtil.DISCORD_EPOCH) << net.dv8tion.jda.api.utils.TimeUtil.TIMESTAMP_OFFSET;

        String cmdPrefix = "";

        if (messages.size() > 0) {
            Guild g = messages.get(0).getGuild();
            Server s = AlloyUtil.loadServer(g);
            cmdPrefix = s.getPrefix();
        }

        for (Message msg : messages) {
            if (deletedCount == finalDeleteLimit)
                return false;

            if (msg.isPinned() || msg.getIdLong() < twoWeeksAgo)
                return true;

            boolean all = false;
            boolean bot = false;
            boolean user = false;
            boolean command = false;
            boolean matches = false;
            boolean notmatches = false;

            try {
                all = (hasManageMessages || (msg.getAuthor() != null
                        && msg.getAuthor().getId().equals(msg.getJDA().getSelfUser().getId())));
                bot = (hasManageMessages && msg.getAuthor() != null && msg.getAuthor().isBot())
                        || msg.getAuthor() != null && msg.getAuthor().isBot();
                user = finalToDeleteFrom != null && msg.getAuthor() != null
                        && msg.getAuthor().getId().equals(finalToDeleteFrom.getUser().getId());
                command = (msg.getContentRaw().startsWith(cmdPrefix) && hasManageMessages) || (msg.getAuthor() == null
                        || msg.getAuthor().getId().equals(msg.getJDA().getSelfUser().getId()));
                matches = hasManageMessages && msg.getContentRaw().contains(finalDeletePattern);
                notmatches = hasManageMessages && !msg.getContentRaw().contains(finalDeletePattern);
            } catch (Exception ignored) {
            }

            if (finalStyle == PurgeStyle.ALL && all) {
                messagesToDelete.add(msg);
                deletedCount++;
            }

            else if (finalStyle == PurgeStyle.BOTS && bot) {
                messagesToDelete.add(msg);
                deletedCount++;
            }

            else if (finalStyle == PurgeStyle.USER && user) {
                messagesToDelete.add(msg);
                deletedCount++;
            }

            else if (finalStyle == PurgeStyle.COMMANDS && command) {
                messagesToDelete.add(msg);
                deletedCount++;
            }

            else if (finalStyle == PurgeStyle.MATCHES && matches) {
                messagesToDelete.add(msg);
                deletedCount++;
            }

            else if (finalStyle == PurgeStyle.NOTMATCHES && notmatches) {
                messagesToDelete.add(msg);
                deletedCount++;
            }

            else if (finalStyle == PurgeStyle.UNKNOWN) {
                messagesToDelete.add(msg);
                deletedCount++;
            }

        }
        return false;
    }

    private void deleteBulk(Sendable bot, TextChannel channel, boolean hasManageMessages,
            List<Message> messagesToDelete, Queueable q) {
        if (messagesToDelete.isEmpty())
            return;

        if (hasManageMessages) 
        {
            Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>() 
            {
                @Override
                public void accept(ErrorResponseException t) 
                {
                    Alloy.LOGGER.warn("KickCommand", t.getMessage());
                }
    
                @Override
                public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
                {
                    return Consumer.super.andThen(after);
                }
            };
            ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.INVALID_BULK_DELETE, consumer);
    
            for (int index = 0; index < messagesToDelete.size(); index += PurgeCommand.MAX_BULK_SIZE) 
            {
                if (messagesToDelete.size() - index < 2)
                    messagesToDelete.get(index).delete().queue(null,handler);
                else
                    channel.deleteMessages(messagesToDelete.subList(index, Math.min(index + PurgeCommand.MAX_BULK_SIZE, messagesToDelete.size()))).queue(null,handler);
                cooldown(2000l);
            }
            
            AlloyTemplate t = Templates.bulkDeleteSuccessful(channel, messagesToDelete.size() - 1);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
    
            Job j2 = new DeleteMessageJob(sm);
            q.queueIn(j2, 5000l);
        }

        
    }

    private void cooldown(long l) 
    {
        try 
        {
            Thread.sleep(l);
        } 
        catch (Exception ignored){
        }
    }

    private enum PurgeStyle {
        UNKNOWN, ALL, BOTS, USER, MATCHES, NOTMATCHES, COMMANDS
    }
}
