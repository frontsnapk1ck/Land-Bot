package alloy.command.console;

import java.util.List;

import alloy.command.util.AbstractConsoleCommand;
import alloy.input.console.ConsoleInputData;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import utility.StringUtil;

public class DMCommand extends AbstractConsoleCommand {

    public static final int RETRIEVE_SIZE = 50;

    @Override
    public void execute(ConsoleInputData data) 
    {
        List<String> args = data.getArgs();
        
        if (args.size() < 3)
            return;
        
        if (args.get(1).equalsIgnoreCase("reply"))
            reply(data);
        if (args.get(1).equalsIgnoreCase("list"))
            list(data);
        else
            message(data);
    }

    private void list(ConsoleInputData data) 
    {
        List<String> args = data.getArgs();

        User u = null;
        try 
        {
            u = data.getJda().getUserById(args.get(2));
        }
        catch (Exception e){
        }

        if (u == null)
            return;
        
        try
        {
            MessageChannel chan = u.openPrivateChannel().complete();
            List<Message> list = chan.getHistory().retrievePast(RETRIEVE_SIZE).complete();
            String[][] tableData = new String[list.size()][3];

            int i = 0;
            for (Message message : list) 
            {
                tableData[i][0] = message.getContentRaw();
                tableData[i][1] = message.getId();
                tableData[i][2] = message.getAuthor().getAsTag();
                i++;
            }
            String out = StringUtil.makeTable(tableData, new String[]{"~~message~~" , "~~ID~~", "~~author~~"});
            System.err.println(out);
        }
        catch (Exception e) 
        {
            System.out.println("could not list the messages for user: " + u.getAsTag() );
        }
    }

    private void message(ConsoleInputData data) 
    {
        List<String> args = data.getArgs();

        User u = null;
        try 
        {
            u = data.getJda().getUserById(args.get(1));
        }
        catch (Exception e){
        }

        if (u == null)
            return;
        String message = StringUtil.joinStrings(args.toArray(new String[]{}), 2);
        try
        {
            u.openPrivateChannel().complete().sendMessage(message).complete();
            System.out.println("Message Sent to " + u.getAsTag());
        }
        catch (Exception e) 
        {
            System.out.println("message could not be sent to " + u.getAsTag() );
        }
    }

    private void reply(ConsoleInputData data) 
    {
        List<String> args = data.getArgs();

        if (args.size() < 5)
            return;

        User u = null;
        try 
        {
            u = data.getJda().getUserById(args.get(2));
        }
        catch (Exception e){
        }

        if (u == null)
            return;
        String message = StringUtil.joinStrings(args.toArray(new String[]{}), 4);
        try
        {
            MessageChannel chan = u.openPrivateChannel().complete();
            Message m = chan.retrieveMessageById(args.get(3)).complete();
            m.reply(message).complete();
            System.out.println("Reply Sent to " + u.getAsTag());
        }
        catch (Exception e) 
        {
            System.out.println("could not be reply to " + u.getAsTag() + " messageID: " + args.get(3));
        }
    }
    
}
