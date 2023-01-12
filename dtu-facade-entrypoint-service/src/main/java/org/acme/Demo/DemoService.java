package org.acme.Demo;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class DemoService {

    public static final String DEMO_QUEUE_REQ = "demo_req";
    public static final String DEMO_QUEUE = "demo";

    private final MessageQueue queue;

    private final Map<CorrelationId, CompletableFuture<Demo>> correlations = new ConcurrentHashMap<>();

    public DemoService(MessageQueue q) {
        queue = q;
        queue.addHandler(DEMO_QUEUE_REQ, this::handleDemoMessage);
    }

    public Demo getDemoMessage() {
        var correlationId = CorrelationId.randomId();
        correlations.put(correlationId, new CompletableFuture<>());
        Event event = new Event(DEMO_QUEUE_REQ, new Object[] { correlationId });
        queue.publish(event);
        return correlations.get(correlationId).join();
    }

    public void handleDemoMessage(Event e) {
        var demo = e.getArgument(0, Demo.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        correlations.get(correlationid).complete(demo);
    }

}
