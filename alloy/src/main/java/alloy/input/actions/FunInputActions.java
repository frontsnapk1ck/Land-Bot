package alloy.input.actions;

import java.util.ArrayList;
import java.util.List;

import alloy.command.fun.DeadChatCommand;
import alloy.command.fun.LinkCommand;
import alloy.command.fun.RankCommand;
import alloy.command.fun.RemindCommand;
import alloy.command.fun.SayCommand;
import alloy.command.fun.SpamCommand;
import alloy.command.util.AbstractCommand;
import alloy.input.discord.AlloyInputAction;
import alloy.input.discord.AlloyInputData;
import input.InputAction;

public class FunInputActions extends AbstractActions {

    public static final InputAction DEAD_CHAT_ACTION;
    public static final InputAction LINK_ACTION;
    public static final InputAction RANK_ACTION;
    public static final InputAction SAY_ACTION;
    public static final InputAction SPAM_ACTION;
    public static final InputAction REMIND_ACTION;

    static {
        DEAD_CHAT_ACTION = loadDeadChatAction();
        LINK_ACTION = loadLinkAction();
        RANK_ACTION = loadRankAction();
        SAY_ACTION = loadSayAction();
        SPAM_ACTION = loadSpamAction();
        REMIND_ACTION = loadRemindAction();
    }

    private static InputAction loadDeadChatAction() {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new DeadChatCommand();
                command.execute(data);
            }

        };
        return action;
    }

    private static InputAction loadLinkAction() 
    {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new LinkCommand();
                command.execute(data);
            }

        };
        return action;
    }

    private static InputAction loadRemindAction() 
    {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new RemindCommand();
                command.execute(data);
            }

        };
        return action;
    }

    private static InputAction loadSpamAction() {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new SpamCommand();
                command.execute(data);
            }

        };
        return action;
    }

    private static InputAction loadSayAction() {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new SayCommand();
                command.execute(data);
            }

        };
        return action;
    }

    private static InputAction loadRankAction() {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new RankCommand();
                command.execute(data);
            }

        };
        return action;
    }

    @Override
    public List<InputAction> getAllAction() 
    {
        List<InputAction> actions = new ArrayList<InputAction>();

        actions.add(DEAD_CHAT_ACTION);
        actions.add(RANK_ACTION);
        actions.add(SAY_ACTION);
        actions.add(SPAM_ACTION);

        return actions;    
    }
}
