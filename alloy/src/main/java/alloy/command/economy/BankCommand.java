package alloy.command.economy;

import alloy.command.util.AbstractCommand;
import alloy.gameobjects.player.Player;
import alloy.handler.BankHandeler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.io.loader.PlayerLoaderText;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.DisUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BankCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Message msg = data.getMessageActual();
        Guild g = data.getGuild();
        
        PlayerLoaderText plt = new PlayerLoaderText();
        String path = AlloyUtil.getGuildPath(g) + AlloyUtil.USER_FOLDER + AlloyUtil.SUB;
        Player p = null;

        if (args.length == 0)
        {
            path += author.getId();
            p = plt.load(path);
        }
        else if ( DisUtil.findMember(g, args[0]) != null) 
        {
            path += DisUtil.findMember(g, args[0]).getId();
            p = plt.load(path);
        }

        if (p != null)
        {
            Template t = Templates.bankCurrentBalance(p);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("BankCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);            
            return;
        }

        MessageEmbed eb = null;
        if (BankHandeler.isSend(msg))
            eb = handleSend(p , args , channel , author , msg);
        
        if (eb != null)
        {
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("BankCommand");
            sm.setMessage(eb);
            bot.send(sm);   
            return;
        }

        Template t = Templates.argumentsNotRecognized (msg);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("BankCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);   
        return;
        
    }

    private MessageEmbed handleSend(Player p, String[] args, MessageChannel channel, User author, Message msg) 
    {
        String message = "";
        if (BankHandeler.hasMessage(args))
            message = BankHandeler.getMessage(args);
        User targetUser = BankHandeler.getTargetUser(args);
        int amount = BankHandeler.getAmount(args);

        if (amount == BankHandeler.INVALID_FORMAT)
        {
            Template t = Templates.invalidNumberFormat(args);
            return t.getEmbed();
        }

        if (amount < 0)
        {
            Template t = Templates.onlyPositiveNumbers(amount);
            return t.getEmbed();
        }

        if (amount < BankHandeler.MINIUM_BALACE )
        {
            Template t = Templates.bankTransferMinimum();
            return t.getEmbed();
        }

        if (!p.canSpend(amount))
        {
            Template t = Templates.bankInsufficientFunds(author , amount);
            return t.getEmbed();
        }

        if (targetUser == null)
        {
            Template t = Templates.userNotFound( args[0] );
            return t.getEmbed();
        }

        PlayerLoaderText plt = new PlayerLoaderText();
        String path = AlloyUtil.ALLOY_PATH + "res\\servers\\" + msg.getGuild().getId() + "\\users\\" + targetUser.getId();
        Player targetP = plt.load(path);

        targetP.addBal(amount);
        p.spend(amount);

        Template t = Templates.bankTransferSuccess(p , targetP , amount , message);
        return t.getEmbed();
    }
    
}
