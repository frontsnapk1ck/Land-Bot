package alloy.input.actions;

import java.util.ArrayList;
import java.util.List;

import alloy.command.configuration.*;
import alloy.command.util.AbstractCommand;
import alloy.input.discord.AlloyInputAction;
import alloy.input.discord.AlloyInputData;
import input.InputAction;

public class ConfigurationInputActions extends AbstractActions {

    public static final InputAction BUILDING_ACTION;
    public static final InputAction WORK_ACTION;
    public static final InputAction VIEW_ACTION;
    public static final InputAction STARTING_BALANCE_ACTION;
    public static final InputAction COOLDOWN_ACTION;
    public static final InputAction PREFIX_ACTION;
    public static final InputAction XP_BLACKLIST_ACTION;
    public static final InputAction SET_ACTION;
    
    
    static {
        BUILDING_ACTION         = loadBuildingAction();
        WORK_ACTION             = loadWorkAction();
        VIEW_ACTION             = loadViewAction();
        STARTING_BALANCE_ACTION = loadStartingBalanceAction();
        COOLDOWN_ACTION         = loadCooldownAction();
        PREFIX_ACTION           = loadPrefixAction();
        XP_BLACKLIST_ACTION     = loadXPBlacklistAction();
        SET_ACTION              = loadSetAction();
    }

    private static InputAction loadBuildingAction() 
    {
        InputAction action = new AlloyInputAction()
        {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new BuildingCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadXPBlacklistAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new XPBlacklistCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadSetAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new SetCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadPrefixAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new PrefixCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadCooldownAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new CooldownCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadStartingBalanceAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new StartingBalanceCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadViewAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new ViewCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadWorkAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new WorkCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    @Override
    public List<InputAction> getAllAction() 
    {
        List<InputAction> actions = new ArrayList<InputAction>();

        actions.add(BUILDING_ACTION);
        actions.add(WORK_ACTION);
        actions.add(VIEW_ACTION);
        actions.add(STARTING_BALANCE_ACTION);
        actions.add(COOLDOWN_ACTION);
        actions.add(PREFIX_ACTION);

        return actions;    
    }
}
