package alloy.input.console;

import java.util.ArrayList;
import java.util.List;

import input.Input;
import input.InputAction;
import input.InputSystem;
import net.dv8tion.jda.api.JDA;

public class ConsoleInputSystem extends InputSystem {

    @Override
    protected void processInput( Input input ) 
    {
        JDA jda = getJDA( input );
        List<String> strings = getString(input);

        input = stripArgs(input);

        String id = getInputID(input);
        if ( id == null )
            return;

        InputAction action = getInputAction(id);
        if ( action == null )
            return;
            
        if ( jda == null )
            executeAction( action );
        else
            executeAction( action, jda , strings );
    }

    private List<String> getString(Input input) 
    {
        String trigger = input.getTrigger();
        String[] args = trigger.split(" ");
        List<String> strings = new ArrayList<String>();
        for (String s : args)
            strings.add(s);
        
        return strings;

    }

    private void executeAction(InputAction inputAction, JDA jda, List<String> strings) 
    {
        if (inputAction instanceof ConsoleInputAction )
        {
            ConsoleInputAction a = (ConsoleInputAction) inputAction;
            a.execute(strings, jda);
            return;
        }
        inputAction.execute();
    }

    private JDA getJDA(Input input) 
    {
        if (input instanceof ConsoleInput)
        {
            ConsoleInput consoleinput = (ConsoleInput) input;
            return consoleinput.getJda();
        }
        return null;
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
