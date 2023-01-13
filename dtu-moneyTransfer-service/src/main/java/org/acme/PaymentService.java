package org.acme;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Payment;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SOURCE: HUBERT BAUMEISTER: STUDENT_REGISTRATION_CORRELATION PROJECT
 */

public class PaymentService {

    MessageQueue queue;
    private Map<CorrelationId, CompletableFuture<Payment>> correlations = new ConcurrentHashMap<>();
    public static final String AMOUNT_REQUESTED = "AmountRequested";
    public static final String AMOUNT_ASSIGNED = "AmountAssigned";

    public PaymentService(MessageQueue q) {
        queue = q;
        queue.addHandler(AMOUNT_ASSIGNED, this::handleAmountAssigned);
    }

    public Payment assignAmount(Payment p) {
        var correlationId = CorrelationId.randomId();
        correlations.put(correlationId, new CompletableFuture<>());
        Event event = new Event(AMOUNT_REQUESTED, new Object[] { p, correlationId });
        queue.publish(event);
        return correlations.get(correlationId).join();

    }

    public void handleAmountAssigned(Event ev) {
        var p = ev.getArgument(0, Payment.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        correlations.get(correlationId).complete(p);
    }

}