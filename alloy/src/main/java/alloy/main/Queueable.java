package alloy.main;

import utility.event.Job;

public interface Queueable {

    
    public void queueIn( Job action , long offset );

    public void queue( Job action );

}
