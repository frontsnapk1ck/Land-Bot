package alloy.input.discord;

import input.InputAction;

public interface AlloyInputAction extends InputAction {
        
    public void execute( AlloyInputData data );

}
