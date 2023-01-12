package org.acme.DemoRec;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;

public class DemoRecService {

    public static final String DEMO_QUEUE_REQ = "demo_req";
    public static final String DEMO_QUEUE = "demo";

    private final MessageQueue queue;
    public DemoRecService(MessageQueue q) {
        queue = q;
        queue.addHandler(DEMO_QUEUE, this::handleDemoMessageReq);
    }

    public String getDemo() {
        Demo demo = new Demo();
        return demo.getDemoMessage();
    }

    public void handleDemoMessageReq(Event e) {
        var demo = e.getArgument(0, Demo.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        demo.setDemoMessage(getDemo());
        Event event = new Event(DEMO_QUEUE_REQ, new Object[] { demo, correlationid });
        queue.publish(event);
    }
}
