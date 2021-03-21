package frontsnapk1ck.alloy.input.actions;

import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.command.voice.ForceSkipCommand;
import frontsnapk1ck.alloy.command.voice.JoinCommand;
import frontsnapk1ck.alloy.command.voice.LeaveCommand;
import frontsnapk1ck.alloy.command.voice.LyricsCommand;
import frontsnapk1ck.alloy.command.voice.NowPlayingCommand;
import frontsnapk1ck.alloy.command.voice.PlayCommand;
import frontsnapk1ck.alloy.command.voice.PlayNowCommand;
import frontsnapk1ck.alloy.command.voice.PlayTopCommand;
import frontsnapk1ck.alloy.command.voice.QueueCommand;
import frontsnapk1ck.alloy.command.voice.RemoveCommand;
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
    public static final InputAction LYRICS_ACTION;
    public static final InputAction PLAY_NOW_ACTION;
    public static final InputAction PLAY_TOP_ACTION;
    public static final InputAction REMOVE_ACTION;

    static {
        JOIN_ACTION = loadJoinAction();
        LEAVE_ACTION = loadLeaveAction();
        
        SKIP_ACTION = loadSkipAction();
        FORCE_SKIP_ACTION = loadForceSkipAction();
        
        QUEUE_ACTION = loadQueueAction();
        NOW_PLAYING_ACTION = loadNowPlayingAction();
        
        PLAY_ACTION = loadPlayAction();
        PLAY_TOP_ACTION = loadPTAction();
        PLAY_NOW_ACTION = loadPNAction();
        
        LYRICS_ACTION = loadLyricsAction();
        REMOVE_ACTION = loadRemoveAction();
    }

    @Override
    public List<InputAction> getAllAction() 
    {
        List<InputAction> actions = new ArrayList<InputAction>();

        

        return actions;
    }

    private static InputAction loadRemoveAction() 
    {
        InputAction action = new AlloyInputAction() 
        {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new RemoveCommand();
                command.execute(data);
            }
        };
        return action;
    }

    private static InputAction loadPTAction() 
    {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new PlayTopCommand();
                command.execute(data);
            }
        };
        return action;
    }

    private static InputAction loadPNAction() 
    {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new PlayNowCommand();
                command.execute(data);
            }
        };
        return action;
    }

    private static InputAction loadLyricsAction() 
    {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new LyricsCommand();
                command.execute(data);
            }
        };
        return action;
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
