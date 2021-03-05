package alloy.input.actions;

import java.util.ArrayList;
import java.util.List;

import alloy.command.console.CacheCommand;
import alloy.command.console.DMCommand;
import alloy.command.console.InviteCommand;
import alloy.command.console.MembersCommand;
import alloy.command.console.NameCommand;
import alloy.command.console.QueueCommand;
import alloy.command.console.RolesCommand;
import alloy.command.console.TestCommand;
import alloy.command.console.UpdateCommand;
import alloy.command.util.AbstractConsoleCommand;
import alloy.input.console.ConsoleInputAction;
import alloy.input.console.ConsoleInputData;
import input.InputAction;

public class ConsoleInputActions extends AbstractActions {

    public static final InputAction NAME_ACTION;
    public static final InputAction MEMBERS_ACTION;
    public static final InputAction GET_INVITES_ACTION;
    public static final InputAction ROLES_ACTION;
    public static final InputAction QUEUE_ACTION;
    public static final InputAction CACHE_ACTION;
    public static final InputAction TEST_ACTION;
    public static final InputAction DM_ACTION;
    public static final InputAction UPDATE_ACTION;

    static{
        NAME_ACTION = loadNameAction();
        MEMBERS_ACTION = loadMembersAction();
        GET_INVITES_ACTION = loadGetInvitesAction();
        ROLES_ACTION = loadRolesAction();
        QUEUE_ACTION = loadQueueAction();
        CACHE_ACTION = loadCacheAction();
        TEST_ACTION = loadTestAction();
        DM_ACTION = loadDMAction();
        UPDATE_ACTION = loadUpdateAction();
    }

    private static InputAction loadNameAction() 
    {
        InputAction action = new ConsoleInputAction()
        {
            @Override
            public void execute() {
            }

            @Override
            public void execute(ConsoleInputData data) 
            {
                AbstractConsoleCommand command = new NameCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadDMAction() 
    {
        InputAction action = new ConsoleInputAction()
        {
            @Override
            public void execute() {
            }

            @Override
            public void execute(ConsoleInputData data) 
            {
                AbstractConsoleCommand command = new DMCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadUpdateAction() 
    {
        InputAction action = new ConsoleInputAction()
        {
            @Override
            public void execute() {
            }

            @Override
            public void execute(ConsoleInputData data) 
            {
                AbstractConsoleCommand command = new UpdateCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadCacheAction() 
    {
        InputAction action = new ConsoleInputAction()
        {
            @Override
            public void execute() {
            }

            @Override
            public void execute(ConsoleInputData data) 
            {
                AbstractConsoleCommand command = new CacheCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadTestAction() 
    {
        InputAction action = new ConsoleInputAction(){
            @Override
            public void execute() {
            }
            @Override
            public void execute(ConsoleInputData data) 
            {
                AbstractConsoleCommand command = new TestCommand();
                command.execute(data);
            }
        };
        return action;
    }

    private static InputAction loadQueueAction() 
    {
        InputAction action = new ConsoleInputAction()
        {
            @Override
            public void execute() {
            }

            @Override
            public void execute(ConsoleInputData data) 
            {
                AbstractConsoleCommand command = new QueueCommand();
                command.execute(data);  
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
            public void execute(ConsoleInputData data) 
            {
                AbstractConsoleCommand command = new RolesCommand();
                command.execute(data);  
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
            public void execute(ConsoleInputData data) 
            {
                AbstractConsoleCommand command = new InviteCommand();
                command.execute(data);  
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
            public void execute(ConsoleInputData data) 
            {
                AbstractConsoleCommand command = new MembersCommand();
                command.execute(data);  
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
