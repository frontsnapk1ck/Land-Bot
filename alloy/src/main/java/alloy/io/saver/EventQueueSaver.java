package alloy.io.saver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import alloy.main.util.SendableMessage;
import alloy.utility.discord.DisUtil;
import alloy.utility.job.jobs.RemindJob;
import io.SaverXML;
import utility.StringUtil;
import utility.event.EventManager.ScheduledJob;
import utility.event.Job;

public class EventQueueSaver extends SaverXML<PriorityBlockingQueue<ScheduledJob>, String> {

    public static final String REMIND_JOB = "RemindJob";
    public static final String TIME = "time";
    public static final String CHANNEL = "channel";
    public static final String MENTION = "mention";
    public static final String MESSAGE = "message";

    public static final Class<?>[] saved;

    static {
        saved = loadSaved();
    }

    private static Class<?>[] loadSaved() {
        return new Class<?>[] { RemindJob.class };
    }

    @Override
    public boolean save(PriorityBlockingQueue<ScheduledJob> queue, String path) 
    {

        Map<Class<?>, List<ScheduledJob>> map = buildMap(queue);

        try 
        {
            Document doc = loadDoc();
            Element rootElement = doc.createElement("JobQueue");
            doc.appendChild(rootElement);

            for (Class<?> c : saved) {
                Element group = doc.createElement(c.getSimpleName() + "Group");
                List<ScheduledJob> toSave = map.get(c);
                if (c == RemindJob.class)
                    saveRemind(doc, group, toSave);
                rootElement.appendChild(group);

            }

            File f = new File(path);
            saveDoc(doc, f);
            return true;

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void saveRemind(Document doc, Element group, List<ScheduledJob> toSave) 
    {
        for (ScheduledJob sJob : toSave) 
        {
            Element remind = doc.createElement(REMIND_JOB);

            RemindJob j = (RemindJob) sJob.job;
            SendableMessage sm = j.getSM();
            String channelS = DisUtil.toString(sm.getChannel());
            String mentionS = sm.getMessage().getContentRaw();
            String[] splitNewLine = sm.getMessage().getEmbeds().get(0).getDescription().split("\n");
            String messageS = StringUtil.joinStrings(splitNewLine, 1);

            Attr time = doc.createAttribute(TIME);
            time.setValue("" + sJob.time);
            
            Attr channel = doc.createAttribute(CHANNEL);
            channel.setValue(channelS);

            Attr mention = doc.createAttribute(MENTION);
            mention.setValue(mentionS);

            Attr message = doc.createAttribute(MESSAGE);
            message.setValue(messageS);

            remind.setAttributeNode(time);
            remind.setAttributeNode(channel);
            remind.setAttributeNode(mention);
            remind.setAttributeNode(message);

            group.appendChild(remind);
        }
    }

    private Map<Class<?>, List<ScheduledJob>> buildMap(PriorityBlockingQueue<ScheduledJob> queue) 
    {
        Map<Class<?> , List<ScheduledJob>> map = new HashMap<Class<?> , List<ScheduledJob>>();
        for (Class<?> c : saved) 
            map.put(c, new ArrayList<ScheduledJob>());
        for (ScheduledJob sJob : queue) 
        {
            Job job = sJob.job;
            for (Class<?> c : saved) 
            {
                if (job.getClass() == c)
                    map.get(c).add(sJob);
            }    
        }
        return map;
        
    }


    
}
