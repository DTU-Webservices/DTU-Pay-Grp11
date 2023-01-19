package org.acme.Services;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Entities.MoneyTransfer;
import org.acme.Entities.Payment;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kristoffer Torngaard Pedersen s205354
 */

public class PaymentService {

    private static final String PAYMENT_CREATE_REQ = "PaymentCreateReq";

    private final MessageQueue queue;

    private final Map<CorrelationId, CompletableFuture<MoneyTransfer>> paymentFuture = new ConcurrentHashMap<>();

    public PaymentService(MessageQueue q) {
        queue = q;
        queue.addHandler("PaymentCreated", this::handlePaymentCreated);
    }

    public MoneyTransfer createPayment(Payment payment) {
        var correlationId = CorrelationId.randomId();
        payment.setPaymentId(correlationId.getId());
        System.out.println("PaymentService: create payment: " + payment);
        paymentFuture.put(correlationId, new CompletableFuture<>());
        Event event = new Event(PAYMENT_CREATE_REQ, new Object[] {payment, correlationId});
        queue.publish(event);
        return paymentFuture.get(correlationId).join();
    }

    public void handlePaymentCreated(Event ev) {
        var mt = ev.getArgument(0, MoneyTransfer.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);

        if (mt.getMtId() == null) {
            mt.setDescription("Transaction failed");
        }

        try {
            paymentFuture.get(correlationId).complete(mt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
