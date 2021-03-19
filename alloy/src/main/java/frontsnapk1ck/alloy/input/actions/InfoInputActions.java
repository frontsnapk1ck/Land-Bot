package frontsnapk1ck.alloy.input.actions;

import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.alloy.command.configuration.PrefixCommand;
import frontsnapk1ck.alloy.command.info.DonateCommand;
import frontsnapk1ck.alloy.command.info.HelpCommand;
import frontsnapk1ck.alloy.command.info.InfoCommand;
import frontsnapk1ck.alloy.command.info.InviteCommand;
import frontsnapk1ck.alloy.command.info.PingCommand;
import frontsnapk1ck.alloy.command.info.UptimeCommand;
import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.input.discord.AlloyInputAction;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.input.InputAction;

public class InfoInputActions extends AbstractActions {

    public static final InputAction HELP_ACTION;
    public static final InputAction INVITE_ACTION;
    public static final InputAction DONATE_ACTION;
    public static final InputAction PING_ACTION;
    public static final InputAction PREFIX_SHOW_ACTION;
    public static final InputAction INFO_ACTION;
    public static final InputAction UPTIME_ACTION;

    static {
        HELP_ACTION = loadHelpAction();
        INVITE_ACTION = loadInviteAction();
        DONATE_ACTION = loadDonateAction();
        PING_ACTION = loadPingAction();
        PREFIX_SHOW_ACTION = LoadPrefixShowAction();
        INFO_ACTION = loadInfoAction();
        UPTIME_ACTION = loadUptimeAction();
    }

    private static InputAction loadUptimeAction() {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new UptimeCommand();
                command.execute(data);
            }
        };
        return action;
    }

    private static InputAction loadDonateAction() 
    {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new DonateCommand();
                command.execute(data);
            }
        };
        return action;
    }

    private static InputAction loadHelpAction() {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new HelpCommand();
                command.execute(data);
            }

        };
        return action;
    }

    private static InputAction loadInviteAction() {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new InviteCommand();
                command.execute(data);
            }

        };
        return action;
    }

    private static InputAction loadPingAction() {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new PingCommand();
                command.execute(data);
            }

        };
        return action;
    }

    private static InputAction LoadPrefixShowAction() {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new PrefixCommand();
                command.execute(data);
            }

        };
        return action;
    }

    private static InputAction loadInfoAction() {
        InputAction action = new AlloyInputAction() {
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) {
                AbstractCommand command = new InfoCommand();
                command.execute(data);
            }

        };
        return action;
    }

    @Override
    public List<InputAction> getAllAction() 
    {
        List<InputAction> actions = new ArrayList<InputAction>();

        actions.add(HELP_ACTION);
        actions.add(INVITE_ACTION);
        actions.add(PING_ACTION);
        actions.add(PREFIX_SHOW_ACTION);
        actions.add(INFO_ACTION);
        actions.add(UPTIME_ACTION);
        
        return actions;
    }

}
