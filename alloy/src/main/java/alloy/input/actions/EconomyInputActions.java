package alloy.input.actions;

import java.util.ArrayList;
import java.util.List;

import alloy.command.economy.BankCommand;
import alloy.command.economy.BuyCommand;
import alloy.command.economy.DayCommand;
import alloy.command.economy.MeCommand;
import alloy.command.economy.PayCommand;
import alloy.command.economy.WorkCommand;
import alloy.command.util.AbstractCommand;
import alloy.input.discord.AlloyInputAction;
import alloy.input.discord.AlloyInputData;
import input.InputAction;

public class EconomyInputActions extends AbstractActions {

    public static final InputAction BANK_ACTION;
    public static final InputAction BUY_ACTION;
    public static final InputAction PAY_ACTION;
    public static final InputAction DAY_ACTION;
    public static final InputAction WORK_ACTION;
    public static final InputAction ME_ACTION;
    
    static{
        BANK_ACTION = loadBankAction();
        BUY_ACTION  = loadBuyAction();
        PAY_ACTION  = loadPayAction();
        DAY_ACTION  = loadDayAction();
        ME_ACTION   = loadMeAction();
        WORK_ACTION = loadWorkAction();        
    }

    private static InputAction loadBankAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new BankCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadBuyAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new BuyCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadMeAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new MeCommand();
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

    private static InputAction loadDayAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new DayCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    private static InputAction loadPayAction() 
    {
        InputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new PayCommand();
                command.execute(data);  
            }

        };
        return action;
    }

    @Override
    public List<InputAction> getAllAction() 
    {
        List<InputAction> actions = new ArrayList<InputAction>();

        actions.add(BANK_ACTION);
        actions.add(PAY_ACTION);
        actions.add(DAY_ACTION);
        actions.add(WORK_ACTION);

        return actions;    
    }
    
}
