package alloy.handler.util;

import java.util.ArrayList;
import java.util.List;

import alloy.main.intefs.Queueable;
import alloy.main.intefs.Sendable;
import alloy.utility.job.jobs.SendMessageJob;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SpamContainer {

    public static final long DELAY = 1250l;
    
    private List<SendMessageJob> messages;
    private Queueable queue;

    private Long id;

    public SpamContainer(int reps, String message, TextChannel c, Long num, Sendable sendable , Queueable queue) 
    {
        this.messages = new ArrayList<SendMessageJob>();

        this.queue = queue;
        configMessages(message,reps,c,sendable);
	}

    private void configMessages(String message, int reps, TextChannel c, Sendable sendable) 
    {
        Message m = new MessageBuilder(message).build();
        for (int i = 0; i < reps; i++) 
        {
            SendMessageJob smj =  new SendMessageJob(sendable, c, m);
            this.messages.add(smj);
            this.queue.queueIn(smj , DELAY * i);
        }
    }

    public void stop() 
    {
        for (SendMessageJob smj : messages) 
        {
            smj.disable();
            this.queue.unQueue(smj);
        }
	}

    public Long getID() 
    {
		return this.id;
	}

    public void setId(Long num) 
    {
        this.id = num;
	}

}
