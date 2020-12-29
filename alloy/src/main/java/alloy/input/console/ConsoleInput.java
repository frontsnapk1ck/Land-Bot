package alloy.input.console;

import input.Input;
import net.dv8tion.jda.api.JDA;

public class ConsoleInput extends Input {

    private JDA jda;

    public ConsoleInput(String name, String trigger, String description,  JDA jda) 
    {
        super(name, trigger);
        this.jda = jda;
    }

    public JDA getJda() {
        return jda;
    }

    public void setJda(JDA jda) {
        this.jda = jda;
    }
    
}
