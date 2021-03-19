package frontsnapk1ck.alloy.input.actions;

import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.alloy.command.level.LeaderboardCommand;
import frontsnapk1ck.alloy.command.level.RankCommand;
import frontsnapk1ck.alloy.command.level.RankupCommand;
import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.input.discord.AlloyInputAction;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.input.InputAction;

public class LevelInputActions extends AbstractActions {

    public static final InputAction LEADERBOARD_ACTION;
    public static final InputAction RANK_ACTION;
    public static final InputAction RANKUP_ACTION;

    static {
        LEADERBOARD_ACTION = loadLeaderboardAction();
        RANK_ACTION = loadRankAction();
        RANKUP_ACTION = loadRankUpAction();
    }


    @Override
    public List<InputAction> getAllAction() 
    {
        List<InputAction> actions = new ArrayList<InputAction>();

        

        return actions;
    }

    private static InputAction loadRankUpAction() 
    {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new RankupCommand();
                command.execute(data);
            }
        };
        return action;
    }

    private static InputAction loadRankAction() 
    {
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

    private static InputAction loadLeaderboardAction() 
    {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new LeaderboardCommand();
                command.execute(data);
            }
        };
        return action;
    }
    
}
