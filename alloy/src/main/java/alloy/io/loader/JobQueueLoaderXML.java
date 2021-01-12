package alloy.io.loader;

import java.util.concurrent.PriorityBlockingQueue;

import org.w3c.dom.Element;

import io.DataLoader;
import utility.event.EventManager.ScheduledJob;

public class JobQueueLoaderXML extends DataLoader<PriorityBlockingQueue<ScheduledJob>, Element> {

}
