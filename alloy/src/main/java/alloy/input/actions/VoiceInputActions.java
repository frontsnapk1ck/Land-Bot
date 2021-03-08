package alloy.input.actions;

import java.util.ArrayList;
import java.util.List;

import alloy.command.util.AbstractCommand;
import alloy.command.voice.JoinCommand;
import alloy.command.voice.LeaveCommand;
import alloy.command.voice.PlayCommand;
import alloy.command.voice.QueueCommand;
import alloy.command.voice.SkipCommand;
import alloy.input.discord.AlloyInputAction;
import alloy.input.discord.AlloyInputData;
import input.InputAction;

public class VoiceInputActions extends AbstractActions {

    public static final InputAction JOIN_ACTION;
    public static final InputAction LEAVE_ACTION;
    public static final InputAction QUEUE_ACTION;
    public static final InputAction SKIP_ACTION;
    public static final InputAction PLAY_ACTION;

    static{
        JOIN_ACTION = loadJoinAction();
        LEAVE_ACTION = loadLeaveAction();
        QUEUE_ACTION = loadQueueAction();
        SKIP_ACTION = loadSkipAction();
        PLAY_ACTION = loadPlayAction();
    }

    @Override
    public List<InputAction> getAllAction() 
    {
        List<InputAction> actions = new ArrayList<InputAction>();

        

        return actions;
    }

    private static InputAction loadQueueAction() 
    {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new QueueCommand();
                command.execute(data);
            }
        };
        return action;
    }

    private static InputAction loadSkipAction() 
    {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new SkipCommand();
                command.execute(data);
            }
        };
        return action;
    }

    private static InputAction loadPlayAction() 
    {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new PlayCommand();
                command.execute(data);
            }
        };
        return action;
    }

    private static InputAction loadLeaveAction() 
    {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new LeaveCommand();
                command.execute(data);
            }
        };
        return action;
    }

    private static InputAction loadJoinAction() 
    {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new JoinCommand();
                command.execute(data);
            }
        };
        return action;
    }
    
}
