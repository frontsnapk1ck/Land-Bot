package frontsnapk1ck.alloy.input.actions;

import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.command.voice.ForceSkipCommand;
import frontsnapk1ck.alloy.command.voice.JoinCommand;
import frontsnapk1ck.alloy.command.voice.LeaveCommand;
import frontsnapk1ck.alloy.command.voice.NowPlayingCommand;
import frontsnapk1ck.alloy.command.voice.PlayCommand;
import frontsnapk1ck.alloy.command.voice.QueueCommand;
import frontsnapk1ck.alloy.command.voice.SkipCommand;
import frontsnapk1ck.alloy.input.discord.AlloyInputAction;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.input.InputAction;

public class VoiceInputActions extends AbstractActions {

    public static final InputAction JOIN_ACTION;
    public static final InputAction LEAVE_ACTION;
    public static final InputAction QUEUE_ACTION;
    public static final InputAction NOW_PLAYING_ACTION;
    public static final InputAction SKIP_ACTION;
    public static final InputAction FORCE_SKIP_ACTION;
    public static final InputAction PLAY_ACTION;

    static{
        JOIN_ACTION = loadJoinAction();
        LEAVE_ACTION = loadLeaveAction();
        QUEUE_ACTION = loadQueueAction();
        NOW_PLAYING_ACTION = loadNowPlayingAction();
        SKIP_ACTION = loadSkipAction();
        FORCE_SKIP_ACTION = loadForceSkipAction();
        PLAY_ACTION = loadPlayAction();
    }

    @Override
    public List<InputAction> getAllAction() 
    {
        List<InputAction> actions = new ArrayList<InputAction>();

        

        return actions;
    }

    private static InputAction loadForceSkipAction() 
    {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new ForceSkipCommand();
                command.execute(data);
            }
        };
        return action;
    }

    private static InputAction loadNowPlayingAction() 
    {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new NowPlayingCommand();
                command.execute(data);
            }
        };
        return action;
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
