package frontsnapk1ck.alloy.input.actions;

import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.alloy.command.administration.BanCommand;
import frontsnapk1ck.alloy.command.administration.CaseCommand;
import frontsnapk1ck.alloy.command.administration.KickCommand;
import frontsnapk1ck.alloy.command.administration.MuteCommand;
import frontsnapk1ck.alloy.command.administration.PurgeCommand;
import frontsnapk1ck.alloy.command.administration.UnmuteCommand;
import frontsnapk1ck.alloy.command.administration.WarnCommand;
import frontsnapk1ck.alloy.command.administration.WarningsCommand;
import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.input.discord.AlloyInputAction;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.input.InputAction;

/**
 * the container for all the Administrative {@link InputAction}s
 * @note this uses a static block to set the {@link InputAction}s
 *          to what the value should be. pretty much all of the methods
 *          are the same the only difference is the command that is loaded 
 *          in the action
 */
public class AdministrationInputActions extends AbstractActions {

    /** the input action for the {@link PurgeCommand} */
    public static final InputAction PURGE_ACTION;

    /** the input action for the {@link BanCommand} */
    public static final InputAction BAN_ACTION;

    /** the input action for the {@link CaseCommand} */
    public static final InputAction CASE_ACTION;

    /** the input action for the {@link KickCommand} */
    public static final InputAction KICK_ACTION;

    /** the input action for the {@link UnmuteCommand} */
    public static final InputAction UNMUTE_ACTION;

    /** the input action for the {@link WarnCommand} */
    public static final InputAction WARN_ACTION;

    /** the input action for the {@link MuteCommand} */
    public static final InputAction MUTE_ACTION;

    /** the input action for the {@link WarningsCommand} */
    public static final InputAction WARNINGS_ACTION;
    

    /**
     * the static block that loads all of the command to what they should be
     */
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

    
    /** 
     * loads the input action for the {@link PurgeCommand} 
     * @return the input action for the {@link PurgeCommand}
     */
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

    /** 
     * loads the input action for the {@link WarningsCommand} 
     * @return the input action for the {@link WarningsCommand}
     */
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

    /** 
     * loads the input action for the {@link MuteCommand} 
     * @return the input action for the {@link MuteCommand}
     */
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

    /** 
     * loads the input action for the {@link WarnCommand} 
     * @return the input action for the {@link WarnCommand}
     */
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

    /** 
     * loads the input action for the {@link UnmuteCommand} 
     * @return the input action for the {@link UnmuteCommand}
     */
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

    /** 
     * loads the input action for the {@link KickCommand} 
     * @return the input action for the {@link KickCommand}
     */
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

    /** 
     * loads the input action for the {@link CaseCommand} 
     * @return the input action for the {@link CaseCommand}
     */
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

    /** 
     * loads the input action for the {@link BanCommand} 
     * @return the input action for the {@link BanCommand}
     */
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