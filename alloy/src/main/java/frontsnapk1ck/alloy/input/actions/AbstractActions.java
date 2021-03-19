package frontsnapk1ck.alloy.input.actions;

import java.util.List;

import frontsnapk1ck.input.InputAction;

/**
 * the abstract class that all input action must extend
 */
public abstract class AbstractActions {

    /**
     * 
     * @return a list of all the {@link InputAction}s in the set of Actions
     *          in the form of a {@link List}
     */
    public abstract List<InputAction> getAllAction();
    
}
