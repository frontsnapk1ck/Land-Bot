package alloy.input.actions;

import java.util.ArrayList;
import java.util.List;

import alloy.command.console.InviteCommand;
import alloy.command.console.MembersCommand;
import alloy.command.console.NameCommand;
import alloy.command.console.RolesCommand;
import alloy.command.util.AbstractConsoleCommand;
import alloy.input.console.ConsoleInputAction;
import input.InputAction;
import net.dv8tion.jda.api.JDA;

public class ConsoleInputActions extends AbstractActions {

    public static final InputAction NAME_ACTION;
    public static final InputAction MEMBERS_ACTION;
    public static final InputAction GET_INVITES_ACTION;
    public static final InputAction ROLES_ACTION;

    static{
        NAME_ACTION = loadNameAction();
        MEMBERS_ACTION = loadMembersAction();
        GET_INVITES_ACTION = loadGetInvitesAction();
        ROLES_ACTION = loadRolesAction();
    }

    private static InputAction loadNameAction() 
    {
        InputAction action = new ConsoleInputAction()
        {
            @Override
            public void execute() {
            }

            @Override
            public void execute(List<String> args, JDA jda) 
            {
                AbstractConsoleCommand command = new NameCommand();
                command.execute(args, jda);  
            }

        };
        return action;
    }

    private static InputAction loadRolesAction() 
    {
        InputAction action = new ConsoleInputAction()
        {
            @Override
            public void execute() {
            }

            @Override
            public void execute(List<String> args , JDA jda) 
            {
                AbstractConsoleCommand command = new RolesCommand();
                command.execute(args, jda);  
            }

        };
        return action;
    }

    private static InputAction loadGetInvitesAction() 
    {
        InputAction action = new ConsoleInputAction()
        {
            @Override
            public void execute() {
            }

            @Override
            public void execute(List<String> args, JDA jda) 
            {
                AbstractConsoleCommand command = new InviteCommand();
                command.execute(args, jda);  
            }

        };
        return action;
    }

    private static InputAction loadMembersAction() 
    {
        InputAction action = new ConsoleInputAction()
        {
            @Override
            public void execute() {
            }

            @Override
            public void execute(List<String> args , JDA jda) 
            {
                AbstractConsoleCommand command = new MembersCommand();
                command.execute(args , jda);  
            }

        };
        return action;
    }

    @Override
    public List<InputAction> getAllAction() 
    {
        List<InputAction> actions = new ArrayList<InputAction>();

        actions.add(NAME_ACTION);
        actions.add(MEMBERS_ACTION);
        actions.add(GET_INVITES_ACTION);
        actions.add(ROLES_ACTION);

        return actions;    
    }

}
