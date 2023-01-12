package org.acme.DemoRec;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;

public class DemoRecService {

    public static final String DEMO_QUEUE_REQ = "reqDemo";
    public static final String DEMO_QUEUE = "demo";

    private final MessageQueue queue;
    public DemoRecService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler(DEMO_QUEUE_REQ, this::handleDemoMessageReq);
    }

    public String getDemo() {
        return "This is a message from the Demo!!!!";
    }

    public void handleDemoMessageReq(Event e) {
        var demo = e.getArgument(0, Demo.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        demo.setDemoMessage(getDemo());
        Event event = new Event(DEMO_QUEUE, new Object[] { demo, correlationid });
        queue.publish(event);
    }
}
