package alloy.input.actions;

import java.util.ArrayList;
import java.util.List;

import alloy.command.fun.DeadChatCommand;
import alloy.command.fun.RankCommand;
import alloy.command.fun.SayCommand;
import alloy.command.fun.SpamCommand;
import alloy.command.util.AbstractCommand;
import alloy.input.discord.AlloyInputAction;
import alloy.input.discord.AlloyInputData;
import input.InputAction;

public class FunInputActions extends AbstractActions {

    public static final InputAction DEAD_CHAT_ACTION;
    public static final InputAction RANK_ACTION;
    public static final InputAction SAY_ACTION;
    public static final InputAction SPAM_ACTOIN;

    static {
        DEAD_CHAT_ACTION = loadDeadChatAction();
        RANK_ACTION = loadRankAction();
        SAY_ACTION = loadSayAction();
        SPAM_ACTOIN = loadSpamAction();
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
        actions.add(SPAM_ACTOIN);

        return actions;    
    }
}
