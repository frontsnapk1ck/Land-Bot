package alloy.input.console;

import java.util.List;

import input.Input;
import input.InputAction;
import input.InputSystem;

public class ConsoleInputSystem extends InputSystem {

    @Override
    protected void processInput( Input input ) 
    {
        ConsoleInputData data = null;
        if (hadInputData(input))
            data = getInputData(input);

        input = stripArgs(input);

        String id = getInputID(input);
        if ( id == null )
            return;

        InputAction action = getInputAction(id);
        if ( action == null )
            return;
            
        if ( data == null )
            executeAction( action );
        else
            executeAction( action, data );
    }

    private boolean hadInputData(Input input) 
    {
        return (input instanceof ConsoleInput);
    }

    private ConsoleInputData getInputData(Input input) 
    {
        ConsoleInput in = (ConsoleInput) input;
        return in.getData();
    }

    private void executeAction(InputAction inputAction, ConsoleInputData data) 
    {
        if (inputAction instanceof ConsoleInputAction )
        {
            ConsoleInputAction a = (ConsoleInputAction) inputAction;
            a.execute(data);
            return;
        }
        inputAction.execute();
    }

    private Input stripArgs(Input input) 
    {
        Input bestMatch = findBestMatch(input);
        if (bestMatch != null)
            return bestMatch;
        return input;
    }

    private Input findBestMatch(Input userInput) 
    {
        List<Input> inputs = this.getInputMap().getInputs();
        String[] tArgsArr = userInput.getTrigger().split(" ");
        
        Input bestMatch = null;
        int matches = 0;
        for (Input in : inputs) 
        {
            int localMatches = 0;
            String[] argArr = in.getTrigger().split(" ");

            for (int i = 0; i < argArr.length; i++) 
            {
                try 
                {
                    String arg = argArr[i];
                    String tArg = tArgsArr[i];
                    
                    if (arg.equalsIgnoreCase(tArg))
                        localMatches ++;
                }
                catch (IndexOutOfBoundsException ignored) {
                }
            }

            if (localMatches > matches)
            {
                matches = localMatches;
                bestMatch = in;
            }
        }

        return bestMatch;
    }
    
}
