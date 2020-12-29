package alloy.input.discord;

import java.util.List;

import alloy.command.info.PingMeCommand;
import alloy.command.util.AbstractCommand;
import input.Input;
import input.InputAction;
import input.InputSystem;

public class AlloyInputSystem extends InputSystem {

    @Override
    protected void processInput(Input input) 
    {
        AlloyInputData data = null;
        if (hasInputData(input))
            data = getInputArgs(input);
        
        input = stripArgs(input);

        if (isPingMe(data , input))
        {
            executeAction( loadPingMeAction() , data );
            return;
        }

        String id = getInputID(input);
        if ( id == null )
            return;

        InputAction action = getInputAction(id);
        if ( action == null )
            return;

        if (data == null)
            executeAction(action);
        else
            executeAction((AlloyInputAction) action, data);
    }



    private boolean isPingMe(AlloyInputData data, Input input) 
    {
        String message = input.getTrigger();
        Long selfID = data.getJDA().getSelfUser().getIdLong();
        String selfPing = "<@" + selfID + ">";
        String selfPingAlias = "<@!" + selfID + ">";
        boolean ping =  message.equalsIgnoreCase(selfPing) ||
                        message.equalsIgnoreCase(selfPingAlias);
        return ping;
    }

    private boolean hasInputData(Input input) 
    {
        return (input instanceof AlloyInput);
    }

    private void executeAction(AlloyInputAction action, AlloyInputData data) {
        action.execute(data);
    }

    private AlloyInputData getInputArgs(Input input) 
    {
        AlloyInput in = (AlloyInput) input;
        return in.getData();        
    }

    // private Input findBestMatch(Input userInput) 
    // {
    //     List<Input> inputs = this.getInputMap().getInputs();
    //     String[] tArgsArr = userInput.getTrigger().split(" ");
        
    //     Input bestMatch = null;
    //     int matches = 0;
    //     for (Input in : inputs) 
    //     {
    //         int localMatches = 0;
    //         String[] argArr = in.getTrigger().split(" ");

    //         for (int i = 0; i < argArr.length; i++) 
    //         {
    //             try 
    //             {
    //                 String arg = argArr[i];
    //                 String tArg = tArgsArr[i];
                    
    //                 if (arg.equalsIgnoreCase(tArg))
    //                     localMatches ++;
    //             }
    //             catch (IndexOutOfBoundsException ignored) {
    //             }
    //         }

    //         if (localMatches > matches)
    //         {
    //             matches = localMatches;
    //             bestMatch = in;
    //         }
    //         else if ( localMatches == matches && localMatches != 0)
    //         {
    //             //find percentage match
    //             String oldTrigger = bestMatch.getTrigger();
    //             String newTrigger = in.getTrigger();
    //             String[] oldArr = oldTrigger.split(" ");
    //             String[] newArr = newTrigger.split(" ");

    //             int oldShare = 0;
    //             int newShare = 0;

    //             for (int i = 0; i < tArgsArr.length; i++) 
    //             {
    //                 if (tArgsArr[i].equalsIgnoreCase(oldArr[i])) 
    //                     oldShare ++;
    //                 if (tArgsArr[i].equalsIgnoreCase(newArr[i]))
    //                     newShare ++;
    //             }

    //             float oldPer = (float) tArgsArr.length / (float) oldShare;
    //             float newPer = (float) tArgsArr.length / (float) newShare;

    //             if (newPer > oldPer)
    //                 bestMatch = in;
    //         }
    //     }

    //     return bestMatch;
    // }

    private Input stripArgs(Input input) 
    {
        Input bestMatch = findBestMatch(input);
        if (bestMatch != null)
            return bestMatch;
        return input;
    }

    private Input findBestMatch(Input input) 
    {
        String first = input.getTrigger().split(" ")[0];
        List<Input> inputs = this.getInputMap().getInputs();
        for (Input in : inputs) 
        {
            String inFist = in.getTrigger().split(" ")[0];
            if (inFist.equalsIgnoreCase(first))
                return in;
        }
        return null;
    }

    private AlloyInputAction loadPingMeAction() 
    {
        AlloyInputAction action = new AlloyInputAction(){
            @Override
            public void execute() {
            }

            @Override
            public void execute(AlloyInputData data) 
            {
                AbstractCommand command = new PingMeCommand();
                command.execute(data);  
            }

        };
        return action; 
    }
    
}
