package input;

public class InputSystem implements InputListener {

    private InputMap inputMap;
    private ActionMap actionMap;

    @Override
    public void onInput(Input input) 
    {
        processInput(input);
    }

    protected void processInput(Input input) 
    {
        String id = getInputID(input);
        if ( id == null )
            return;

        InputAction action = getInputAction(id);
        if ( action == null )
            return;
            
        executeAction(action);
    }

    protected void executeAction(InputAction action)
    {
        action.execute();
    }

    protected InputAction getInputAction(String id) 
    {
        return this.actionMap.get(id);
    }

    protected String getInputID(Input input) 
    {
        return this.inputMap.get(input);
    }

    public void setInputMap(InputMap inputMap) 
    {
        this.inputMap = inputMap;
	}

    public void setActionMap(ActionMap actionMap) 
    {
        this.actionMap = actionMap;
    }

    public ActionMap getActionMap() {
        return actionMap;
    }

    public InputMap getInputMap() {
        return inputMap;
    }
    
    public boolean hasInputMap()
    {
        return this.inputMap != null;
    }

    public boolean hasActionMap()
    {
        return this.actionMap != null;
    }

    public ActionMap removeActionMap()
    {
        ActionMap map = this.actionMap;
        this.actionMap = null;
        return map;
    }

    public InputMap removeInputMap()
    {
        InputMap map = this.inputMap;
        this.inputMap = null;
        return map;
    }
    
}
