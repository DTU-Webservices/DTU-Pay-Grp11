package org.acme.Demo;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class DemoService {

    public static final String DEMO_QUEUE_REQ = "reqDemo";
    public static final String DEMO_QUEUE = "demo";

    private final MessageQueue queue;

    private final Map<CorrelationId, CompletableFuture<Demo>> correlations = new ConcurrentHashMap<>();

    public DemoService(MessageQueue q) {
        queue = q;
        queue.addHandler(DEMO_QUEUE, this::handleDemoMessage);
    }

    public Demo getDemoMessage() {
        Demo demo = new Demo();
        var correlationId = CorrelationId.randomId();
        correlations.put(correlationId, new CompletableFuture<>());
        Event event = new Event(DEMO_QUEUE_REQ, new Object[] { demo, correlationId });
        queue.publish(event);
        return correlations.get(correlationId).join();
    }

    public void handleDemoMessage(Event ev) {
        var demo = ev.getArgument(0, Demo.class);
        var correlationid = ev.getArgument(1, CorrelationId.class);
        correlations.get(correlationid).complete(demo);
    }

}
