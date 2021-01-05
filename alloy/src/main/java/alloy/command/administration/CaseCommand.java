package alloy.command.administration;

import alloy.command.util.AbstractCommand;
import alloy.gameobjects.Case;
import alloy.gameobjects.Server;
import alloy.handler.CaseHandeler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.StringUtil;
import utility.Util;


public class CaseCommand extends AbstractCommand {

    @Override
    public String getDescription()
    {
        return "Moderate the mod-cases";
    }

    @Override
    public String getCommand()
    {
        return "case";
    }

    @Override
    public String[] getUsage()
    {
        return new String[] { 
                "case reason <id> <message>  //sets/modifies the reason of a case",
                "case reason last <message>  //sets/modified the reason of the last added case by you",
                "case user <name/id/mention> //shows a list of cases for this user",
                "case show <id/username>     //shows case" };
    }

    @Override
    public String[] getAliases()
    {
        return new String[] {};
    }

    @Override
    public DisPerm getPermission() 
    {
        return DisPerm.ADMINISTRATOR;
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild guild = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (!DisPermUtil.checkPermission(guild.getMember(author), getPermission())) 
        {
            Template t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("AbstractModerationCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        boolean show = args.length > 0 && args[0].equalsIgnoreCase("show") && args.length < 3;
        boolean reason = args.length > 0 && args[0].equalsIgnoreCase("reason");
        boolean validInt = Util.validInt(args[1]);
        if (reason)
        {
            MessageEmbed embed = editReason(bot, guild, guild.getMember(author), channel, args[1], StringUtil.joinStrings(args, 2));

            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("AbstractModerationCommand");
            sm.setMessage(embed);
            bot.send(sm);
            return;
        }

        if (show && validInt)
        {
            Case c = CaseHandeler.getCase(guild.getIdLong(), args[1] );
            MessageEmbed e;
            if (c == null)
                e = Templates.caseNotFound(args[1]).getEmbed();
            else
                e = CaseHandeler.toEmbed(c);
            
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("AbstractModerationCommand");
            sm.setMessage(e);
            bot.send(sm);
            return;
        }

        
        Template t = Templates.invalidUse(channel);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("AbstractModerationCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        return;
    }

    private MessageEmbed editReason(Sendable bot, Guild guild, Member moderator, MessageChannel channel, String caseId, String reason) 
    {
        Case c = CaseHandeler.getCase( guild.getIdLong() , caseId );
        if (c == null)
        {
            Template t = Templates.caseNotFound(caseId);
            return t.getEmbed();
        }

        c.setReason(reason);

        Server s = AlloyUtil.loadServer(guild);
        TextChannel tc = guild.getTextChannelById(s.getModLogChannel());
        if (tc == null)
        {
            Template t = Templates.modlogNotFound();
            return t.getEmbed();
        }

        Message m = tc.getHistory().getMessageById(c.getMessageId());
        m.editMessage(CaseHandeler.toEmbed(c)).queue();
        
        Template t = Templates.caseReasonModified(reason);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("AbstractModerationCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        
        return null;
    }
    
}
