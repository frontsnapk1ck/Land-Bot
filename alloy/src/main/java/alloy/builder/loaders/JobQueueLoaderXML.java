package alloy.builder.loaders;

import java.util.concurrent.PriorityBlockingQueue;

import org.w3c.dom.Element;

import alloy.builder.DataLoader;
import utility.event.EventManager.ScheduledJob;

public class JobQueueLoaderXML extends DataLoader<PriorityBlockingQueue<ScheduledJob>, Element> {

}
