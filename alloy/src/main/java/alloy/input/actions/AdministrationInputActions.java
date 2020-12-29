package alloy.input.actions;

import java.util.ArrayList;
import java.util.List;

import alloy.command.administration.BanCommand;
import alloy.command.administration.CaseCommand;
import alloy.command.administration.KickCommand;
import alloy.command.administration.MuteCommand;
import alloy.command.administration.PurgeCommand;
import alloy.command.administration.UnmuteCommand;
import alloy.command.administration.WarnCommand;
import alloy.command.administration.WarningsCommand;
import alloy.command.util.AbstractCommand;
import alloy.input.discord.AlloyInputAction;
import alloy.input.discord.AlloyInputData;
import input.InputAction;

public class AdministrationInputActions extends AbstractActions {

    public static final InputAction PURGE_ACTION;
    public static final InputAction BAN_ACTION;
    public static final InputAction CASE_ACTION;
    public static final InputAction KICK_ACTION;
    public static final InputAction UNMUTE_ACTION;
    public static final InputAction WARN_ACTION;
    public static final InputAction MUTE_ACTION;
    public static final InputAction WARNINGS_ACTION;
    

    static{
        PURGE_ACTION     = loadPurgeAction();
        BAN_ACTION       = loadBanAction();
        CASE_ACTION      = loadCaseAction();
        KICK_ACTION      = loadKickAction();
        UNMUTE_ACTION    = loadUnmuteAction();
        WARN_ACTION      = loadWarnAction();
        MUTE_ACTION      = loadMuteAction();
        WARNINGS_ACTION  = loadWarningsAction();
    }

    private static InputAction loadPurgeAction() 
    {
        InputAction action = new AlloyInputAction()
        {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new PurgeCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadWarningsAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new WarningsCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadMuteAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new MuteCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadWarnAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new WarnCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadUnmuteAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new UnmuteCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadKickAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new KickCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadCaseAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new CaseCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadBanAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new BanCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    @Override
    public List<InputAction> getAllAction() 
    {
        List<InputAction> actions = new ArrayList<InputAction>();

        actions.add(PURGE_ACTION);
        actions.add(BAN_ACTION);
        actions.add(CASE_ACTION);
        actions.add(KICK_ACTION);
        actions.add(UNMUTE_ACTION);
        actions.add(WARN_ACTION);
        actions.add(MUTE_ACTION);
        actions.add(WARNINGS_ACTION);

        return actions;    
    }
    
}